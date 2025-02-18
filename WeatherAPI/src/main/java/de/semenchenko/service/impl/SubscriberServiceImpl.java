package de.semenchenko.service.impl;

import de.semenchenko.dto.SubscriberDTO;
import de.semenchenko.service.SubscriberService;
import de.semenchenko.service.WeatherDistributor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    private final WeatherDistributor weatherDistributor;

    public SubscriberServiceImpl(WeatherDistributorImpl weatherDistributor) {
        this.weatherDistributor = weatherDistributor;
    }

    @Override
    public Mono<Void> subscribe(SubscriberDTO subscriberDTO) {
        weatherDistributor.startPush(subscriberDTO.getCallBackUrl());

        return Mono.empty();
    }
}
