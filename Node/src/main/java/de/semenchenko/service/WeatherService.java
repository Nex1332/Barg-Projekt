package de.semenchenko.service;

import reactor.core.publisher.Mono;

public interface WeatherService {
    Mono<Boolean> isCityValid(String city);
}
