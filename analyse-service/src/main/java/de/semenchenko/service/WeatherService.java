package de.semenchenko.service;

import de.semenchenko.service.dto.WeatherDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeatherService {
    public Mono<Void> validate(Flux<WeatherDTO> weatherDTOFlux);
}
