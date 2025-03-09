package de.semenchenko.repository;

import de.semenchenko.entity.Place;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ReactivePlaceRepository extends R2dbcRepository<Place, Long> {
    Mono<Place> findByCityId(Long cityId);
}
