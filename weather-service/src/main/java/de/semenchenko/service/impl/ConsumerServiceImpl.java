package de.semenchenko.service.impl;

import de.semenchenko.AppUserDTO;
import de.semenchenko.service.ConsumerService;
import de.semenchenko.service.MainService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;

@Log4j
@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final KafkaReceiver<String, AppUserDTO> kafkaAppUserReceiver;
    private final MainService mainService;

    public ConsumerServiceImpl(KafkaReceiver<String, AppUserDTO> kafkaAppUserReceiver, MainService mainService) {
        this.kafkaAppUserReceiver = kafkaAppUserReceiver;
        this.mainService = mainService;
        consumeWeatherRequests();
    }

    @Override
    public void consumeWeatherRequests() {
        mainService.processWeatherRequests(kafkaAppUserReceiver.receive()
                .doOnError(e -> log.error("Consuming is failed:" + e)));
    }
}
