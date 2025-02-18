package de.semenchenko.service;

import de.semenchenko.dto.SubscriberDTO;
import reactor.core.publisher.Mono;

public interface SubscriberService {
    Mono<Void> subscribe(SubscriberDTO subscriberDTO);
}
