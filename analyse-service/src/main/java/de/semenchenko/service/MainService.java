package de.semenchenko.service;

import de.semenchenko.entity.City;
import de.semenchenko.entity.Weather;
import reactor.core.publisher.Flux;

public interface MainService {
    public Flux<String> getCityFlux();
}
