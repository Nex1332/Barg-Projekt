package de.semenchenko.service.impl;

import de.semenchenko.MailParams;
import de.semenchenko.dao.ReactiveAppUserDAO;
import de.semenchenko.entity.AppUser;
import de.semenchenko.entity.enums.BotState;
import de.semenchenko.entity.enums.UserState;
import de.semenchenko.service.AppUserService;
import de.semenchenko.service.WeatherService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Mono;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.Duration;

@Log4j
@Service
public class AppUserServiceImpl implements AppUserService {
    @Value("${spring.data.redis.ttl}")
    private Long TTL;
    @Value("${spring.data.redis.hash.key}")
    private String HASH_KEY;
    private final ReactiveAppUserDAO reactiveAppUserDAO;
    private final ReactiveRedisOperations<String, AppUser> appUserOps;
    private final WeatherService weatherService;
    private final WebClient activationWebClient;

    public AppUserServiceImpl(ReactiveAppUserDAO reactiveAppUserDAO, WeatherService weatherService,
                              ReactiveRedisOperations<String, AppUser> appUserOps,
                              @Qualifier("activationWebClient") WebClient activationWebClient) {
        this.reactiveAppUserDAO = reactiveAppUserDAO;
        this.weatherService = weatherService;
        this.appUserOps = appUserOps;
        this.activationWebClient = activationWebClient;
    }

    @Override
    public Mono<AppUser> findCachedAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        var id = telegramUser.getId();

        return appUserOps.opsForValue().get(HASH_KEY + id)
                .switchIfEmpty(
                        findOrSaveAppUser(telegramUser, update)
                                .flatMap(transientAppUser ->
                                        appUserOps.opsForValue()
                                                .set(HASH_KEY + id,
                                                        transientAppUser, Duration.ofSeconds(TTL))
                                                .thenReturn(transientAppUser)
                                )
                );
    }

    @Override
    public Mono<String> registerUser(AppUser appUser) {
        var appUserState = appUser.getUserState();

        if (appUserState.equals(UserState.REGISTERED.toString())) {
            return Mono.just("You are already registered");
        } else if (appUserState.equals(UserState.CONFIRM_EMAIL.toString())) {
            return Mono.just("You need to confirm your email in the message that was sent to you by email: ");
        } else if (appUserState.equals(UserState.CONFIRM_CITY.toString())) {
            return Mono.just("You need to write the name of city you would like to receive information about");
        } else {
            appUser.setBotState(BotState.WAIT_FOR_EMAIL_STATE.toString());
            return updateAppUser(appUser)
                    .then(Mono.just("Send your email! ;)"));
        }
    }

    @Override
    public Mono<String> setCancel(AppUser appUser) {
        appUser.setBotState(BotState.BASE_STATE.toString());
        return updateAppUser(appUser)
                .then(Mono.just("The command is canceled! ;)"));
    }

    @Override
    public Mono<String> setEmail(AppUser appUser, String email) {
        try {
            var emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return Mono.just("Please enter a correct email address. To cancel the command, type /cancel.");
        }

        if (appUser.getEmail() == null) {
            appUser.setEmail(email);
            appUser.setUserState(UserState.CONFIRM_EMAIL.toString());

            return reactiveAppUserDAO.save(appUser)
                    .flatMap(savedAppUser -> sendRequestToMailService(email, appUser.getTelegramUserId())
                            .flatMap(response -> {
                                if (response.getStatusCode() != HttpStatus.OK) {
                                    var msg = String.format("Sending email to %s failed.", email);
                                    log.error(msg);

                                    savedAppUser.setEmail(null);
                                    return updateAppUser(savedAppUser)
                                            .then(Mono.just(msg));
                                }

                                return Mono.just("A letter has been sent to you by email.\n" +
                                        "Follow the link in the email to confirm your registration.");
                            }));
        } else {
            return Mono.just("This email is already in use. Enter a valid email."
                    + " To cancel the command, enter /cancel");
        }
    }

    @Override
    public Mono<String> setCity(AppUser appUser, String city) {
        return weatherService.isCityValid(city)
                .flatMap(isValid -> {
                    if (isValid) {
                        appUser.setCity(city);
                        appUser.setUserState(UserState.REGISTERED.toString());
                        appUser.setBotState(BotState.BASE_STATE.toString());
                        return updateAppUser(appUser)
                                .then(Mono.just("""
                                        The city is saved!;)
                                        """));
                    } else return Mono
                            .just("""
                            This city is not supported by our application :(
                            We're sorry!
                            You can try entering a different city or cancel registration by typing /reset
                            """);
                });
    }

    @Override
    public Mono<String> setReset(AppUser appUser) {
        appUser.setUserState(UserState.NOT_REGISTERED.toString());
        appUser.setBotState(BotState.BASE_STATE.toString());
        appUser.setEmail(null);
        appUser.setCity(null);
        return updateAppUser(appUser)
                .then(Mono.just("Your settings have been reset"));
    }

    private Mono<Boolean> updateAppUser(AppUser appUser) {
        return reactiveAppUserDAO.save(appUser)
                .flatMap(transientAppUser -> appUserOps.opsForValue()
                        .set(HASH_KEY + transientAppUser.getTelegramUserId(),
                                transientAppUser, Duration.ofSeconds(TTL)));
    }

    private Mono<ResponseEntity<Void>> sendRequestToMailService(String email, Long telegramUserId) {
        return activationWebClient.post()
                .bodyValue(new MailParams(telegramUserId, email))
                .retrieve()
                .toBodilessEntity();
    }

    private Mono<AppUser> findOrSaveAppUser(User telegramUser, Update update) {
        return reactiveAppUserDAO.findAppUserByTelegramUserId(telegramUser.getId())
                .switchIfEmpty(Mono.defer(() -> {
                    AppUser transientAppUser = AppUser.builder()
                            .telegramUserId(telegramUser.getId())
                            .chatId(update.getMessage().getChatId())
                            .username(telegramUser.getUserName())
                            .userState(UserState.NOT_REGISTERED.toString())
                            .botState(BotState.BASE_STATE.toString())
                            .build();
                    return reactiveAppUserDAO.save(transientAppUser);
                }));
    }
}
