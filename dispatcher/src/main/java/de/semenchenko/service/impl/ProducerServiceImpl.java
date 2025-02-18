package de.semenchenko.service.impl;

import de.semenchenko.service.ProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final KafkaTemplate<String, Update> kafkaTemplate;

    public ProducerServiceImpl(KafkaTemplate<String, Update> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void nodeRequest(Update update) {
        kafkaTemplate.send("node-requests", update);
    }
}
