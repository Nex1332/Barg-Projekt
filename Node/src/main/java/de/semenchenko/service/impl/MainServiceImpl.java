package de.semenchenko.service.impl;

import de.semenchenko.dao.ReactiveAppUserDAO;
import de.semenchenko.model.AppUser;
import de.semenchenko.model.enums.BotState;
import de.semenchenko.model.enums.UserState;
import de.semenchenko.service.AppUserService;
import de.semenchenko.service.MainService;
import de.semenchenko.service.ProducerService;
import de.semenchenko.service.enums.ServiceCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;

@Service
public class MainServiceImpl implements MainService {
    @Value("${spring.data.redis.ttl}")
    private Long TTL;
    @Value("${spring.data.redis.hash.key}")
    private String HASH_KEY;
    private final ReactiveAppUserDAO reactiveAppUserDAO;
    private final ProducerService producerService;
    private final AppUserService appUserService;
    private final ReactiveRedisTemplate<String, AppUser> reactiveRedisTemplate;

    public MainServiceImpl(ReactiveAppUserDAO reactiveAppUserDAO, ProducerService producerService, AppUserService appUserService, ReactiveRedisTemplate<String, AppUser> reactiveRedisTemplate) {
        this.reactiveAppUserDAO = reactiveAppUserDAO;
        this.producerService = producerService;
        this.appUserService = appUserService;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public void distributeByType(Flux<ReceiverRecord<String, Update>> updates) {
        updates.flatMap(receiverRecord -> {
            Update update = receiverRecord.value();
            return findCachedAppUser(update)
                    .flatMap(appUser -> {
                        var botState = appUser.getBotState();
                        var message = update.getMessage();
                        var text = message.getText();
                        var serviceCommand = ServiceCommand.fromValue(text);

                        if (serviceCommand != null) {
                            return serviceCommandProcess(update, serviceCommand, appUser);
                        } else if (botState.equals(BotState.WAIT_FOR_EMAIL_STATE)) {
                            return appUserService.setEmail(appUser, text);
                        } else if (botState.equals(BotState.WAIT_FOR_CITY_STATE)) {
                            return appUserService.setCity(appUser, text);
                        } else {
                            return setUnsupportedMessage(update);
                        }
                    })
                    .doOnNext(ignored -> receiverRecord.receiverOffset().acknowledge())
                    .then();
        });
    }

    private Mono<Void> serviceCommandProcess(Update update, ServiceCommand serviceCommand, AppUser appUser) {
        return switch (serviceCommand) {
            case HELP -> handleHelpCommand(update);
            case REG -> handleRegistrationCommand(update, appUser);
            case START -> handleStartCommand(update);
            case WEATHER -> handleWeatherCommand(update);
            case SETTING -> handleSettingsCommand(update);
            case CANCEL -> handleCancelCommand(update);
        };
    }

    private Mono<Void> setUnsupportedMessage(Update update) {
        return sendAnswer("This message is unsupported", update);
    }

    private Mono<Void> handleWeatherCommand(Update update) {
        return sendAnswer("Temporarily unavailable", update);
    }

    private Mono<Void> handleSettingsCommand(Update update) {
        return sendAnswer("Temporarily unavailable", update);
    }

    private Mono<Void> handleCancelCommand(Update update) {
        return sendAnswer("Temporarily unavailable", update);
    }

    private Mono<Void> handleHelpCommand(Update update) {
        return sendAnswer("Temporarily unavailable", update);
    }

    private Mono<Void> handleStartCommand(Update update) {
        var output = """
                Hello, this is a weather forecast app.
                You can register, using command /reg, and after registering with your phone number and email address,\s
                you will receive recommendations and warnings about changing weather in your city ;)
                You can also check the current weather in your city right now by typing /weather.

                If you need help, just type /help.
                Wishing you good weather ;)""";
        return sendAnswer(output, update);
    }

    private Mono<Void> handleRegistrationCommand(Update update, AppUser appUser) {
        return appUserService.registerUser(appUser)
                .flatMap(output -> sendAnswer(output, update));
    }

    private Mono<Void> sendAnswer(String output, Update update) {
        var sendMessage = new SendMessage();
        sendMessage.setText(output);
        sendMessage.setChatId(update.getMessage().getChatId());
        return producerService.sendSendMessage(sendMessage).then();
    }

    private Mono<AppUser> findCachedAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        var id = telegramUser.getId();
        String redisKey = HASH_KEY + ":" + id;

        return reactiveRedisTemplate.opsForValue().get(redisKey)
                .switchIfEmpty(
                        findOrSaveAppUser(telegramUser)
                                .flatMap(transientAppUser ->
                                        reactiveRedisTemplate.opsForValue()
                                                .set(redisKey, transientAppUser, Duration.ofSeconds(TTL))
                                                .thenReturn(transientAppUser)
                                )
                );
    }

    private Mono<AppUser> findOrSaveAppUser(User telegramUser) {
        return reactiveAppUserDAO.findById(telegramUser.getId())
                .switchIfEmpty(Mono.defer(() -> {
                    AppUser transientAppUser = AppUser.builder()
                            .telegramUserId(telegramUser.getId())
                            .username(telegramUser.getUserName())
                            .email("")
                            .userState(UserState.NOT_REGISTERED)
                            .botState(BotState.BASE_STATE)
                            .build();
                    return reactiveAppUserDAO.save(transientAppUser);
                }));
    }
}
