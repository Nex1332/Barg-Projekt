package de.semenchenko.service;

import de.semenchenko.service.dto.UpdateMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import reactor.core.publisher.Mono;

public interface ProducerService {
    Mono<Void> sendUpdateMessage(String topic, UpdateMessage updateMessage);

    Mono<Void> sendSendMessage(SendMessage sendMessage);
}
