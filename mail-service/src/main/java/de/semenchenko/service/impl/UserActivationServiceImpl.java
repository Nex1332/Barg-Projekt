package de.semenchenko.service.impl;

import de.semenchenko.dao.ReactiveAppUserDAO;
import de.semenchenko.entity.AppUser;
import de.semenchenko.entity.enums.BotState;
import de.semenchenko.entity.enums.UserState;
import de.semenchenko.service.ProducerService;
import de.semenchenko.service.UserActivationService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Log4j
@Service
public class UserActivationServiceImpl implements UserActivationService {
    @Value("${spring.data.redis.ttl}")
    private Long TTL;
    @Value("${spring.data.redis.hash.key}")
    private String HASH_KEY;
    private final ReactiveAppUserDAO reactiveAppUserDAO;
    private final ReactiveRedisOperations<String, AppUser> appUserOps;
    private final ProducerService producerService;

    public UserActivationServiceImpl(ReactiveAppUserDAO reactiveAppUserDAO, ReactiveRedisOperations<String, AppUser> appUserOps, ProducerService producerService) {
        this.reactiveAppUserDAO = reactiveAppUserDAO;
        this.appUserOps = appUserOps;
        this.producerService = producerService;
    }

    @Override
    public Mono<Boolean> activation(Long telegramUserId) {
        return findCachedAppUser(telegramUserId)
                .flatMap(appUser -> {
                    if (!appUser.getUserState().equals(UserState.REGISTERED.toString()) ||
                            !appUser.getUserState().equals(UserState.CONFIRM_CITY.toString())) {

                        appUser.setBotState(BotState.WAIT_FOR_CITY_STATE.toString());
                        appUser.setUserState(UserState.CONFIRM_CITY.toString());

                        return reactiveAppUserDAO.save(appUser)
                                .flatMap(transientAppUser ->
                                        appUserOps
                                                .opsForValue()
                                                .set(HASH_KEY + transientAppUser.getTelegramUserId(),
                                                        transientAppUser, Duration.ofSeconds(TTL)))
                                .flatMap(isCached  -> {
                                    var text = """
                                        Email is confirmed!
                                        Send the city you would like to receive information about
                                        the city can be changed later by typing /settings""";

                                    return sendAnswer(text, appUser.getChatId())
                                            .then(Mono.just(isCached));
                                });
                    } else return Mono.just(false);
                })
                .switchIfEmpty(Mono.just(false));
    }

    private Mono<Void> sendAnswer(String output, Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(output);
        sendMessage.setChatId(chatId);
        return producerService.sendSendMessage(sendMessage);
    }

    private Mono<AppUser> findCachedAppUser(Long telegramUserId) {
        return appUserOps.opsForValue().get(HASH_KEY + telegramUserId)
                .switchIfEmpty(
                        findAppUser(telegramUserId)
                                .flatMap(transientAppUser ->
                                        appUserOps.opsForValue()
                                                .set(HASH_KEY + telegramUserId, transientAppUser, Duration.ofSeconds(TTL))
                                                .thenReturn(transientAppUser)
                                )
                );
    }

    private Mono<AppUser> findAppUser(Long telegramUserId) {
        return reactiveAppUserDAO.findAppUserByTelegramUserId(telegramUserId);
    }
}
