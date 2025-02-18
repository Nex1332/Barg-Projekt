package de.semenchenko.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void listerTextMessages(Update update);
}
