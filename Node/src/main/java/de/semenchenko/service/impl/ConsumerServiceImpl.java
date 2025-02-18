package de.semenchenko.service.impl;

import de.semenchenko.service.MainService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.kafka.receiver.KafkaReceiver;

@Service
public class ConsumerServiceImpl {
    private final KafkaReceiver<String, SendMessage> sendMessageReceiver;
    private final KafkaReceiver<String, Update> updateReceiver;
    private final MainService mainService;

    public ConsumerServiceImpl(KafkaReceiver<String, SendMessage> sendMessageReceiver,
                               KafkaReceiver<String, Update> updateReceiver, MainService mainService) {
        this.sendMessageReceiver = sendMessageReceiver;
        this.updateReceiver = updateReceiver;
        this.mainService = mainService;
        consumeSendMessages();
        consumeUpdates();
    }

    private void consumeSendMessages() {
        sendMessageReceiver
                .receive()
                .doOnNext(record -> {
                    SendMessage sendMessage = record.value();
                    record.receiverOffset().acknowledge();
                })
                .subscribe();
    }

    private void consumeUpdates() {
        mainService.distributeByType(updateReceiver.receive());
    }
}
