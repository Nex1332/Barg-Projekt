package de.semenchenko.service.impl;

import de.semenchenko.service.ProducerService;
import de.semenchenko.service.dto.UpdateMessage;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Log4j
@Service
public class ProducerServiceImpl implements ProducerService {
    private final KafkaSender<String, UpdateMessage> kafkaUpdateMessageSender;
    private final KafkaSender<String, SendMessage> kafkaSendMessageSender;

    public ProducerServiceImpl(KafkaSender<String, UpdateMessage> kafkaSender, KafkaSender<String, SendMessage> kafkaSendMessageSender) {
        this.kafkaUpdateMessageSender = kafkaSender;
        this.kafkaSendMessageSender = kafkaSendMessageSender;
    }

    @Override
    public Mono<Void> sendUpdateMessage(String topic, UpdateMessage updateMessage) {
        return kafkaUpdateMessageSender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<>(topic, updateMessage), null)))
                .then()
                .doOnError(e -> System.err.println("Send failed: " + e.getMessage()))
                .doOnTerminate(() -> log.debug("Send attempt finished"));
    }

    @Override
    public Mono<Void> sendSendMessage(SendMessage sendMessage) {
        return kafkaSendMessageSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>("answers", sendMessage)))
                .then()
                .doOnError(e -> log.error("Send failed" + e))
                .doOnTerminate(() -> log.debug("Send attempt finished"));
    }
}
