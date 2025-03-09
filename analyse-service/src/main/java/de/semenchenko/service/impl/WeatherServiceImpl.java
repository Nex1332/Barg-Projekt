package de.semenchenko.service.impl;

import de.semenchenko.entity.Weather;
import de.semenchenko.repository.ReactiveCityRepository;
import de.semenchenko.repository.ReactiveWeatherRepository;
import de.semenchenko.service.WeatherService;
import de.semenchenko.service.dto.WeatherDTO;
import reactor.core.publisher.Mono;

public class WeatherServiceImpl implements WeatherService {
    private final ReactiveCityRepository reactiveCityRepository;
    private final ReactiveWeatherRepository reactiveWeatherRepository;

    public WeatherServiceImpl(ReactiveCityRepository reactiveCityRepository, ReactiveWeatherRepository reactiveWeatherRepository) {
        this.reactiveCityRepository = reactiveCityRepository;
        this.reactiveWeatherRepository = reactiveWeatherRepository;
    }

    @Override
    public Mono<Void> updateWeatherByCityName(String cityName, WeatherDTO weatherDTO) {
        return reactiveCityRepository.findByCityName(cityName)
                .flatMap(city -> {
                    Weather weather = new Weather();
                    weather.setCityId(city.getCityId());
                    weather.setTemp(weatherDTO.getTemp());
                    weather.setTemp(weatherDTO.getTemp());
                    weather.setTemp(weatherDTO.getTemp());
                    weather.setTemp(weatherDTO.getTemp());
                    weather.setTemp(weatherDTO.getTemp());
                });
    }

    @Override
    public Mono<Weather> getWeatherByCityName(String cityName) {
        return null;
    }
}
