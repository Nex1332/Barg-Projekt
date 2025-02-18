package de.semenchenko.dao;

import de.semenchenko.entity.AppUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ReactiveAppUserDAO extends R2dbcRepository<AppUser, Long> {
    Mono<AppUser> findAppUserByTelegramUserId(Long telegramUserId);
}
