package de.semenchenko.service.impl;

import de.semenchenko.service.dto.SubscriberDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j
@Service
public class SubscriptionService {
    private final WebClient weatherAPIWebClient;

    public SubscriptionService(WebClient weatherAPIWebClient) {
        this.weatherAPIWebClient = weatherAPIWebClient;
    }

    @PostConstruct
    public void subscribeOnStartup() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setCallBackUrl("http://localhost:8089/analyse-service/processWeather");

        weatherAPIWebClient.post()
                .uri("/subscribe") // Тут уже относительный путь, базовый задан в конфиге
                .bodyValue(subscriberDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        success -> log.debug("✅ NUCE, subscribe"),
                        error -> log.error("❌ PIZDA: " + error.getMessage())
                );
    }
}
