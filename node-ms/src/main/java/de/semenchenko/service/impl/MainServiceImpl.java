package de.semenchenko.service.impl;

import de.semenchenko.AppUserDTO;
import de.semenchenko.entity.AppUser;
import de.semenchenko.entity.enums.BotState;
import de.semenchenko.service.AppUserService;
import de.semenchenko.service.MainService;
import de.semenchenko.service.ProducerService;
import de.semenchenko.service.enums.ServiceCommand;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

@Log4j
@Service
public class MainServiceImpl implements MainService {
    private final ProducerService producerService;
    private final AppUserService appUserService;

    public MainServiceImpl(ProducerService producerService, AppUserService appUserService) {
        this.producerService = producerService;
        this.appUserService = appUserService;
    }

    @Override
    public void distributeByType(Flux<ReceiverRecord<String, Update>> updates) {
        updates
                .flatMap(receiverRecord -> {
                    var update = receiverRecord.value();

                    return appUserService.findCachedAppUser(update)
                            .flatMap(appUser -> {
                                var botState = appUser.getBotState();
                                var chatId = update.getMessage().getChatId();
                                var text = update.getMessage().getText();

                                Mono<String> outputMono;

                                var serviceCommand = ServiceCommand.fromValue(text);
                                if (serviceCommand != null) {
                                    outputMono = serviceCommandProcess(update, serviceCommand, appUser);
                                } else if (botState.equals(BotState.WAIT_FOR_EMAIL_STATE.toString())) {
                                    outputMono = appUserService.setEmail(appUser, text);
                                } else if (botState.equals(BotState.WAIT_FOR_CITY_STATE.toString())) {
                                    outputMono = appUserService.setCity(appUser, text);
                                } else {
                                    outputMono = Mono.just("This Message is unsupported");
                                }

                                return outputMono
                                        .flatMap(output -> producerService.sendAnswer(output, chatId))
                                        .onErrorResume(e -> {
                                            log.error("Failed to process update ", e);
                                            return producerService.sendAnswer("Something went wrong", chatId);
                                        });
                            });
                })
                .subscribe();
    }

    private Mono<String> serviceCommandProcess(Update update, ServiceCommand serviceCommand, AppUser appUser) {
        return switch (serviceCommand) {
            case HELP -> handleHelpCommand();
            case REG -> handleRegistrationCommand(appUser);
            case START -> handleStartCommand();
            case WEATHER -> handleWeatherCommand(appUser);
            case SETTING -> handleSettingsCommand(update, appUser);
            case CANCEL -> handleCancelCommand(appUser);
            case RESET -> handleResetCommand(appUser);
        };
    }

    private Mono<String> handleResetCommand(AppUser appUser) {
        return appUserService.setReset(appUser);
    }

    private Mono<String> handleCancelCommand(AppUser appUser) {
        return appUserService.setCancel(appUser);
    }

    private Mono<String> handleSettingsCommand(Update update, AppUser appUser) {
        return Mono.just("temporarily unavailable");
    }

    private Mono<String> handleWeatherCommand(AppUser appUser) {
        return producerService.sendWeatherRequest(
                AppUserDTO.builder()
                        .userState(appUser.getUserState())
                        .botState(appUser.getBotState())
                        .city(appUser.getCity())
                        .email(appUser.getEmail())
                        .chatId(appUser.getChatId())
                        .build()
                )
                .then(Mono.just("We got it;)"));
    }

    private Mono<String> handleStartCommand() {
        return Mono.just("""
                Hello, this is a weather forecast app.
                You can register, using command /reg, and after registering with your phone number and email address,\s
                you will receive recommendations and warnings about changing weather in your city ;)
                You can also check the current weather in your city right now by typing /weather.

                If you need help, just type /help.
                Wishing you good weather ;)""");
    }

    private Mono<String> handleRegistrationCommand(AppUser appUser) {
        return appUserService.registerUser(appUser);
    }

    private Mono<String> handleHelpCommand() {
        return Mono.just("""
                List of available commands:\s
                /reg - registration of user;
                /cancel - cancel of the processing of the current command;
                /weather - current weather in your city
                /settings - user data settings\s
                Wishing you good weather ;)""");
    }
}
