package de.semenchenko.service;

import de.semenchenko.entity.Weather;
import de.semenchenko.service.dto.WeatherDTO;
import reactor.core.publisher.Mono;

public interface WeatherService {
    public Mono<Void> updateWeatherByCityName(String cityName, WeatherDTO weatherDTO);

    public Mono<Weather> getWeatherByCityName(String cityName);
}
