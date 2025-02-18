package de.semenchenko.service.impl;

import de.semenchenko.service.WeatherProducer;
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

        return Weather.builder()
                .temp(15 + random.nextDouble() * 10) // температура в диапазоне от 15 до 25°C
                .feels_like(15 + random.nextDouble() * 10) // ощущаемая температура в диапазоне от 15 до 25°C
                .temp_min(10 + random.nextDouble() * 5) // минимальная температура в диапазоне от 10 до 15°C
                .temp_max(20 + random.nextDouble() * 10) // максимальная температура в диапазоне от 20 до 30°C
                .pressure(1000 + random.nextDouble() * 50) // давление в диапазоне от 1000 до 1050 гПа
                .humidity(40 + random.nextDouble() * 60) // влажность в диапазоне от 40 до 100%
                .sea_level(1000 + random.nextDouble() * 500) // уровень моря в диапазоне от 1000 до 1500 м
                .ground_level(900 + random.nextDouble() * 100) // уровень грунта в диапазоне от 900 до 1000 м
                .build();
    }
}
