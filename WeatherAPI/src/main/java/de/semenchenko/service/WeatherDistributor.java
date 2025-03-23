package de.semenchenko.service;

import de.semenchenko.dto.SubscriberDTO;
import reactor.core.publisher.Mono;

public interface WeatherDistributor {
    Mono<Void> startPush(SubscriberDTO subscriberDTO);
}
