package de.semenchenko.controller;

import de.semenchenko.service.ProducerService;
import de.semenchenko.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
public class UpdateController {
    private final TelegramBot telegramBot;
    private final ProducerService producerService;
    private final MessageUtils messageUtils;

    public UpdateController(TelegramBot telegramBot, ProducerService producerService, MessageUtils messageUtils) {
        this.telegramBot = telegramBot;
        this.producerService = producerService;
        this.messageUtils = messageUtils;
    }

    public void processUpdate(Update update) {
        var message = update.getMessage();

        if (message.hasText()) {
            producerService.nodeRequest(update);
            echo(update);
        } else {
            setUnsupportedMessageType(update);
        }
    }

    private void setUnsupportedMessageType(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "This type of message is unsupported");
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void echo(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Dispatcher echo: " + update.getMessage().getText());
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
