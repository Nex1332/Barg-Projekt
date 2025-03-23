package de.semenchenko.service;

import reactor.core.publisher.Mono;

public interface PlaceService {
    public Mono<Void> updateTablePlace(String city);
}
