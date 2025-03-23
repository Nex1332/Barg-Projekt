package de.semenchenko.service.impl;

import de.semenchenko.service.WeatherProducer;
import de.semenchenko.service.enums.WeatherConditions;
import de.semenchenko.service.model.Weather;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.*;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class WeatherProducerImpl implements WeatherProducer {

    @Override
    public Flux<Weather> generateWeatherFlux(Flux<String> cityMono) {
        return cityMono.next()
                .flatMapMany(city -> Flux.fromStream(IntStream.range(0, 14 * 12)
                .mapToObj(i -> {
                    LocalDateTime startTime = LocalDateTime.now();
                    return getWeather(city, startTime.plusHours(i * 2));
                })));
    }

    @Override
    public Weather getCurrentWeather(String city) {
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

    private Weather getWeather(String city, LocalDateTime localDateTime) {
        Random random = new Random();
        WeatherConditions[] weatherConditions = WeatherConditions.values();
        WeatherConditions randomWeatherCondition = weatherConditions[random.nextInt(weatherConditions.length)];

        return Weather.builder()
                .temp(-10 + random.nextDouble() * 40)
                .feels_like(-10 + random.nextDouble() * 40)
                .temp_min(-10 + random.nextDouble() * 20)
                .temp_max(10 + random.nextDouble() * 20)
                .weatherCondition(randomWeatherCondition.name())
                .city(city)

                .build();
    }
}
