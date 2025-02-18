package de.semenchenko.service.impl;

import de.semenchenko.service.WeatherDistributor;
import de.semenchenko.service.WeatherProducer;
import de.semenchenko.service.model.Weather;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j
@Service
public class WeatherDistributorImpl implements WeatherDistributor {
    private final WeatherProducer weatherProducer;
    private final WebClient.Builder webClientBuilder;

    public WeatherDistributorImpl(WeatherProducerImpl weatherProducer, WebClient.Builder webClientBuilder) {
        this.weatherProducer = weatherProducer;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public void startPush(String callBackUrl) {
        webClientBuilder.build()
                .post()
                .uri(callBackUrl)
                .contentType(MediaType.APPLICATION_NDJSON) // Для потоковой передачи JSON
                .body(weatherProducer.weatherFlux(), Weather.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(throwable -> log.error("Error occurred: " + throwable.getMessage()))
                .subscribe();
    }
}
