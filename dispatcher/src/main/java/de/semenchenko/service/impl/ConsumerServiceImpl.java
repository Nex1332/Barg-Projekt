package de.semenchenko.service.impl;

import de.semenchenko.controller.TelegramBot;
import de.semenchenko.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final TelegramBot telegramBot;

    @Override
    @KafkaListener(topics = "answers", groupId = "answer-group")
    public void consumeAnswers(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
