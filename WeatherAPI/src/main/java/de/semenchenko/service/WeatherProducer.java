package de.semenchenko.service;

import de.semenchenko.service.model.Weather;
import reactor.core.publisher.Flux;

public interface WeatherProducer {
    Flux<Weather> weatherFlux();

    Weather randomWeather();
}
