package de.semenchenko.service;

import de.semenchenko.service.model.Weather;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeatherProducer {
    Flux<Weather> generateWeatherFlux(Flux<String> cityMono);

    Weather getCurrentWeather(String city);
}
