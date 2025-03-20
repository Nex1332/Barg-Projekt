package de.semenchenko.service.impl;

import de.semenchenko.entity.City;
import de.semenchenko.entity.Weather;
import de.semenchenko.repository.ReactiveCityRepository;
import de.semenchenko.repository.ReactiveWeatherRepository;
import de.semenchenko.service.WeatherService;
import de.semenchenko.service.dto.WeatherDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;

public class WeatherServiceImpl implements WeatherService {
    private final ReactiveWeatherRepository reactiveWeatherRepository;
    private final ReactiveCityRepository reactiveCityRepository;

    public WeatherServiceImpl(ReactiveWeatherRepository reactiveWeatherRepository, ReactiveCityRepository reactiveCityRepository) {
        this.reactiveWeatherRepository = reactiveWeatherRepository;
        this.reactiveCityRepository = reactiveCityRepository;
    }

    @Override
    public Mono<Void> validate(Flux<WeatherDTO> weatherDTOFlux) {
        return weatherDTOFlux.filter(weatherDTO ->
                        isRightDay(weatherDTO.getDate().getDayOfWeek()) || isRightWeather(weatherDTO.getWeatherCondition()))
                .flatMap(this::updateWeatherByCityName)
                .then();
    }
// TODO поправить
    private boolean isRightWeather(String weatherCondition) {
        return true;
    }

    private boolean isRightDay(DayOfWeek dayOfWeek) {
        return (dayOfWeek.equals(DayOfWeek.FRIDAY) ||
                dayOfWeek.equals(DayOfWeek.SATURDAY) ||
                dayOfWeek.equals(DayOfWeek.SUNDAY));
    }

    private Mono<Weather> updateWeatherByCityName(WeatherDTO weatherDTO) {
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
                .switchIfEmpty(reactiveCityRepository.save(City.builder().cityName(cityName).build())
                            .then(reactiveWeatherRepository.save(Weather.builder().city(cityName).build())));
    }
}
