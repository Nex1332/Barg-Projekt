package de.semenchenko.controller;

import de.semenchenko.service.MainService;
import de.semenchenko.service.dto.WeatherDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SubscribeController {
    private final MainService mainService;

    public SubscribeController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Void> processWeather(@RequestBody Flux<WeatherDTO> weatherFlux) {
        return mainService.checkWeather(weatherFlux);
    }
}
