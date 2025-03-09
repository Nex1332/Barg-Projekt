package de.semenchenko.service.impl;

import de.semenchenko.service.MainService;
import de.semenchenko.service.dto.WeatherDTO;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MainServiceImpl implements MainService {

    @Override
    public Mono<Void> save(Flux<WeatherDTO> weather) {

    }
}
