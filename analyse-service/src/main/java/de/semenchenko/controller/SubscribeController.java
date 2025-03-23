package de.semenchenko.controller;

import de.semenchenko.service.AnalyseService;
import de.semenchenko.service.WeatherService;
import de.semenchenko.service.dto.WeatherDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/analyse-service")
public class SubscribeController {
    private final AnalyseService analyseService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SubscribeController(AnalyseService analyseService) {
        this.analyseService = analyseService;
    }

    @PostMapping(value = "/processWeather",consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Void> processWeather(@RequestBody Flux<WeatherDTO> weatherFlux) {
        return analyseService.analyse(weatherFlux);
    }
}
