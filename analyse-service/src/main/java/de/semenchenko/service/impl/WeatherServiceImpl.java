package de.semenchenko.service.impl;

import de.semenchenko.entity.Weather;
import de.semenchenko.repository.ReactiveWeatherRepository;
import de.semenchenko.service.WeatherService;
import de.semenchenko.service.dto.WeatherDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final ReactiveWeatherRepository reactiveWeatherRepository;

    public WeatherServiceImpl(ReactiveWeatherRepository reactiveWeatherRepository) {
        this.reactiveWeatherRepository = reactiveWeatherRepository;
    }

    @Override
    public Mono<Weather> updateWeatherByCityName(WeatherDTO weatherDTO) {
        return findOrSaveWeather(weatherDTO.getCity())
                .next()
                .flatMap(weather -> {
                    weather.setTemp(weatherDTO.getTemp());
                    weather.setTemp_max(weatherDTO.getTemp_max());
                    weather.setTemp_min(weatherDTO.getTemp_min());
                    weather.setWeatherCondition(weatherDTO.getWeatherCondition());

                    return reactiveWeatherRepository.save(weather);
                });
    }

    private Flux<Weather> findOrSaveWeather(String cityName) {
        return reactiveWeatherRepository.findByCity(cityName)
                .switchIfEmpty(reactiveWeatherRepository.save(Weather.builder().city(cityName).build()));
    }
}
