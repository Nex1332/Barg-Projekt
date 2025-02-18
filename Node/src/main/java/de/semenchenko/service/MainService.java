package de.semenchenko.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

public interface MainService {
    void distributeByType(Flux<ReceiverRecord<String, Update>> updates);
}
