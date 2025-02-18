package de.semenchenko.dao;

import de.semenchenko.model.AppUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ReactiveAppUserDAO extends R2dbcRepository<AppUser, Long> {
}
