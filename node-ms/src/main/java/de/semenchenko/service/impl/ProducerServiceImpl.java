package de.semenchenko.service.impl;

import de.semenchenko.AppUserDTO;
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
    private final KafkaSender<String, AppUserDTO> kafkaAppUserSender;

    @Override
    public Mono<Void> sendAnswer(String output, Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);

        return kafkaSendMessageSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>("answers", sendMessage)))
                .then()
                .doOnError(e -> log.error("Send failed " + e))
                .doOnTerminate(() -> log.debug("Send attempt finished"));
    }

//    TODO Добавить норм проверку ошибок. Пиздец, Автор ЕБАНАТА
    @Override
    public Mono<Void> sendWeatherRequest(AppUserDTO appUser) {
        return kafkaAppUserSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>("weather-requests", appUser)))
                .then()
                .doOnError(e -> log.error("Send failed: " + e))
                .then(Mono.defer(() -> sendAnswer("Something went wrong", appUser.getChatId())))
                .doOnTerminate(() -> log.debug("Send attempt finished"));
    }
}
