package de.semenchenko.service;

import de.semenchenko.entity.AppUser;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

public interface AppUserService {
    Mono<AppUser> findCachedAppUser(Update update);

    Mono<String> setEmail(AppUser appUser, String email);

    Mono<String> setCity(AppUser appUser, String text);

    Mono<String> registerUser(AppUser appUser);

    Mono<String> setCancel(AppUser appUser);

    Mono<String> setReset(AppUser appUser);
}
