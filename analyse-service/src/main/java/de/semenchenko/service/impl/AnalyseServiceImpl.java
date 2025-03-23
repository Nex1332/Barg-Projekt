package de.semenchenko.service.impl;

import de.semenchenko.service.AnalyseService;
import de.semenchenko.service.WeatherService;
import de.semenchenko.service.dto.WeatherDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;

@Service
public class AnalyseServiceImpl implements AnalyseService {
    private final WeatherService weatherService;

    public AnalyseServiceImpl(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public Mono<Void> analyse(Flux<WeatherDTO> weatherDTOFlux) {
        return weatherDTOFlux.filter(weatherDTO ->
                        isRightDay(weatherDTO.getDate().getDayOfWeek()) || isRightWeather(weatherDTO.getWeatherCondition()))
                .flatMap(weatherService::updateWeatherByCityName)
                .then();
    }

    private boolean isRightWeather(String weatherCondition) {
        return true;
    }

    private boolean isRightDay(DayOfWeek dayOfWeek) {
        return (dayOfWeek.equals(DayOfWeek.FRIDAY) ||
                dayOfWeek.equals(DayOfWeek.SATURDAY) ||
                dayOfWeek.equals(DayOfWeek.SUNDAY));
    }
}
