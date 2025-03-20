package de.semenchenko.repository;

import de.semenchenko.entity.Place;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ReactivePlaceRepository extends R2dbcRepository<Place, Long> {
    Flux<Place> findByCity(String city);
}
