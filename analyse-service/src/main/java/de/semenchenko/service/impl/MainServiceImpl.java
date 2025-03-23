package de.semenchenko.service.impl;

import de.semenchenko.entity.AppUser;
import de.semenchenko.entity.City;
import de.semenchenko.repository.ReactiveAppUserRepository;
import de.semenchenko.repository.ReactiveCityRepository;
import de.semenchenko.service.MainService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MainServiceImpl implements MainService {
    private final ReactiveAppUserRepository reactiveAppUserRepository;

    public MainServiceImpl(ReactiveAppUserRepository reactiveAppUserRepository) {
        this.reactiveAppUserRepository = reactiveAppUserRepository;
    }

    @Override
    public Flux<String> getCityFlux() {
        return reactiveAppUserRepository.findAll()
                .map(AppUser::getCity)
                .distinct();
    }
}
