package de.semenchenko.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProducerService {
    void nodeRequest(Update update);
}
