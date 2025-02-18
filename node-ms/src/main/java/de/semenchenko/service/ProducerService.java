package de.semenchenko.service;

import de.semenchenko.AppUserDTO;
import reactor.core.publisher.Mono;

public interface ProducerService {
    Mono<Void> sendAnswer(String output, Long chatId);

    //    TODO Добавить норм проверку ошибок. Пиздец, Автор ЕБАНАТА
    Mono<Void> sendWeatherRequest(AppUserDTO appUser);
}
