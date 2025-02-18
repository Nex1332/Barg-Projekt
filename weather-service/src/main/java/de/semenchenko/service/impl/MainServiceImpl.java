package de.semenchenko.service.impl;

import de.semenchenko.AppUserDTO;
import de.semenchenko.service.MainService;
import de.semenchenko.service.ProducerService;
import de.semenchenko.service.model.Weather;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

@Service
public class MainServiceImpl implements MainService {
    private final WebClient weatherAPIWebClient;
    private final ProducerService producerService;

    public MainServiceImpl(@Qualifier("weatherAPIWebClient") WebClient weatherAPIWebClient, ProducerService producerService) {
        this.weatherAPIWebClient = weatherAPIWebClient;
        this.producerService = producerService;
    }

    @Override
    public void processWeatherRequests(Flux<ReceiverRecord<String, AppUserDTO>> requests) {
        requests
                .flatMap(receivedRecord -> {
                    var appUser = receivedRecord.value();
                    var city = appUser.getCity();

                    return sendWeatherResponse(weatherAPIWebClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/get-random-weather")
                                    .queryParam("city", city)
                                    .build())
                            .retrieve()
                            .bodyToMono(Weather.class), appUser.getChatId());
                })
                .subscribe();
    }

    @Override
    public Mono<Void> sendAnswer(String output, Long chatId) {
        var sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(output);

        return producerService.sendAnswer(sendMessage);
    }

    private Mono<Void> sendWeatherResponse(Mono<Weather> weathers, Long chatId) {
        return weathers
                .flatMap(weather -> sendAnswer(weather.getWeatherDescription(), chatId));
    }
}
