package de.semenchenko.repository;

import de.semenchenko.entity.City;
import de.semenchenko.entity.Weather;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ReactiveWeatherRepository extends R2dbcRepository<Weather, Long> {
    Mono<Weather> findByCityId(Long cityId);
}
