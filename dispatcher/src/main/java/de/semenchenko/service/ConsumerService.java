package de.semenchenko.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ConsumerService {
    @KafkaListener(topics = "answers", groupId = "answer-group")
    void consumeAnswers(SendMessage sendMessage);
}
