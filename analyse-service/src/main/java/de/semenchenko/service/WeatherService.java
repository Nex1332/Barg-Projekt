package de.semenchenko.service;

import de.semenchenko.entity.Weather;
import de.semenchenko.service.dto.WeatherDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeatherService {
    Mono<Weather> updateWeatherByCityName(WeatherDTO weatherDTO);
}
