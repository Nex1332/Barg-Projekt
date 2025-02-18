package de.semenchenko.service.impl;

import de.semenchenko.service.WeatherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final WebClient weatherAPIWebClient;

    public WeatherServiceImpl(@Qualifier("weatherAPIWebClient") WebClient weatherAPIWebClient) {
        this.weatherAPIWebClient = weatherAPIWebClient;
    }

    @Override
    public Mono<Boolean> isCityValid(String city) {
        return weatherAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", "c3aaabcddb63080b0079bd7df9815848")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> true)
                .onErrorResume(e -> Mono.just(false));
    }
}
