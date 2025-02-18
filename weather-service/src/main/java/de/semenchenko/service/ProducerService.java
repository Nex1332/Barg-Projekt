package de.semenchenko.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import reactor.core.publisher.Mono;

public interface ProducerService {
    Mono<Void> sendAnswer(SendMessage sendMessage);
}
