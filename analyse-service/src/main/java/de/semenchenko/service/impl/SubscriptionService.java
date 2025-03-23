package de.semenchenko.service.impl;

import de.semenchenko.service.MainService;
import de.semenchenko.service.dto.SubscriberDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j
@Service
public class SubscriptionService {
    @Value("${service.callBack.uri}")
    private String callBackUri;
    private final WebClient weatherAPIWebClient;
    private final MainService mainService;

    public SubscriptionService(WebClient weatherAPIWebClient, MainService mainService) {
        this.weatherAPIWebClient = weatherAPIWebClient;
        this.mainService = mainService;
    }

    @PostConstruct
    public void subscribeOnStartup() {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setCallBackUrl(callBackUri);
        subscriberDTO.setCityFlux(mainService.getCityFlux());

        weatherAPIWebClient.post()
                .uri("/getWeatherForTwoNextWeeks")
                .bodyValue(subscriberDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        success -> log.debug("✅ NUCE, subscribe"),
                        error -> log.error("❌ PIZDA: " + error.getMessage())
                );
    }
}
