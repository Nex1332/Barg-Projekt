package de.semenchenko.repository;

import de.semenchenko.entity.Weather;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ReactiveWeatherRepository extends R2dbcRepository<Weather, Long> {
    Flux<Weather> findByCity(String city);
}
