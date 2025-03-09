package de.semenchenko.service.impl;

import de.semenchenko.service.WeatherProducer;
import de.semenchenko.service.enums.WeatherConditions;
import de.semenchenko.service.model.Weather;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Random;

@Service
public class WeatherProducerImpl implements WeatherProducer {

    @Override
    public Flux<Weather> weatherFlux() {
        return Flux.generate(weatherSynchronousSink -> weatherSynchronousSink.next(randomWeather()));
    }

    @Override
    public Weather randomWeather() {
        Random random = new Random();
        WeatherConditions[] weatherConditions = WeatherConditions.values();
        WeatherConditions randomWeatherCondition = weatherConditions[random.nextInt(weatherConditions.length)];

        return Weather.builder()
                .temp(-10 + random.nextDouble() * 40)
                .feels_like(-10 + random.nextDouble() * 40)
                .temp_min(-10 + random.nextDouble() * 20)
                .temp_max(10 + random.nextDouble() * 20)
                .weatherCondition(randomWeatherCondition.name())
                .build();
    }
}
