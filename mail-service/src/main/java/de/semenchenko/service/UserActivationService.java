package de.semenchenko.service;

import reactor.core.publisher.Mono;

public interface UserActivationService {
    Mono<Boolean> activation(Long id);
}
