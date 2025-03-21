package de.semenchenko.service.impl;

import de.semenchenko.service.WeatherProducer;
import de.semenchenko.service.enums.WeatherConditions;
import de.semenchenko.service.model.Weather;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class WeatherProducerImpl implements WeatherProducer {
    private final Map<DayOfWeek ,Integer > daysOfWeek;

    public WeatherProducerImpl() {
        daysOfWeek = new HashMap<>();
        daysOfWeek.put(DayOfWeek.FRIDAY, 1);
        daysOfWeek.put(DayOfWeek.SATURDAY, 2);
        daysOfWeek.put(DayOfWeek.SUNDAY, 3);
    }


    @Override
    public Flux<Weather> weatherFlux() {
        return Flux.generate(weatherSynchronousSink -> weatherSynchronousSink.next(generateWeather()));
    }

//    TODO Изменить HTTP Запрос
    @Override
    public Weather getCurrentWeather() {
        Random random = new Random();
        WeatherConditions[] weatherConditions = WeatherConditions.values();
        WeatherConditions randomWeatherCondition = weatherConditions[random.nextInt(weatherConditions.length)];

        return Weather.builder()
                .temp(-10 + random.nextDouble() * 40)
                .feels_like(-10 + random.nextDouble() * 40)
                .temp_min(-10 + random.nextDouble() * 20)
                .temp_max(10 + random.nextDouble() * 20)
                .weatherCondition(randomWeatherCondition.name())
                .city("Dnipro") // TODO Прикрутить UserID и город

                .build();
    }

    @Override
    public Weather generateWeather() {
        return null;
    }

    private int setRightDaysBound(int month, int year) {
        return Month.of(month).length(Year.isLeap(year));
    }
}
