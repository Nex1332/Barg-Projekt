package de.semenchenko.service.impl;

import de.semenchenko.service.ConsumerService;
import de.semenchenko.service.MainService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.kafka.receiver.KafkaReceiver;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final KafkaReceiver<String, Update> kafkaUpdateReceiver;
    private final MainService mainService;

    public ConsumerServiceImpl(KafkaReceiver<String, Update> kafkaUpdateReceiver, MainService mainService) {
        this.kafkaUpdateReceiver = kafkaUpdateReceiver;
        this.mainService = mainService;
        consumeUpdates();
    }

    @Override
    public void consumeUpdates() {
        mainService.distributeByType(kafkaUpdateReceiver.receive());
    }
}
