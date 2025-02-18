package de.semenchenko.controller;

import de.semenchenko.dto.SubscriberDTO;
import de.semenchenko.service.model.Weather;
import de.semenchenko.service.SubscriberService;
import de.semenchenko.service.impl.WeatherProducerImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Log4j
@RestController
@RequestMapping("/weather-API")
public class WeatherAPIController {
    private final SubscriberService subscriberService;
    private final WeatherProducerImpl weatherProducer;

    public WeatherAPIController(SubscriberService subscriberService, WeatherProducerImpl weatherProducer) {
        this.subscriberService = subscriberService;
        this.weatherProducer = weatherProducer;
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> subscribe(@RequestBody SubscriberDTO subscriberDTO) {
        return subscriberService.subscribe(subscriberDTO);
    }

    @GetMapping("/get-random-weather")
    public Mono<Weather> getRandomWeather(@RequestParam("city") String city) {
        return Mono.just(weatherProducer.randomWeather());
    }
}
