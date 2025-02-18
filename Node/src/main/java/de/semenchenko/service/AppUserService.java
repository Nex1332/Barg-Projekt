package de.semenchenko.service;

import de.semenchenko.model.AppUser;
import reactor.core.publisher.Mono;

public interface AppUserService {
    Mono<String> registerUser(AppUser appUser);
    Mono<String> setEmail(AppUser appUser, String email);
    Mono<String> setCity(AppUser appUser, String city);
}
