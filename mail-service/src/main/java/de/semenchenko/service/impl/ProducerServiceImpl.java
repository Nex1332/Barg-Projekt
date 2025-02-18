package de.semenchenko.service.impl;

import de.semenchenko.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

@Log4j
@RequiredArgsConstructor
@Service
public class ProducerServiceImpl implements ProducerService {
    private final KafkaSender<String, SendMessage> kafkaSendMessageSender;

    @Override
    public Mono<Void> sendSendMessage(SendMessage sendMessage) {
        return kafkaSendMessageSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>("answers", sendMessage)))
                .then()
                .doOnError(e -> log.error("Send failed " + e))
                .doOnTerminate(() -> log.debug("Send attempt finished"));
    }
}
