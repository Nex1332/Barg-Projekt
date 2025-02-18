package de.semenchenko.service;

import de.semenchenko.AppUserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

public interface MainService {
    void processWeatherRequests(Flux<ReceiverRecord<String, AppUserDTO>> requests);

    Mono<Void> sendAnswer(String output, Long chatId);
}
