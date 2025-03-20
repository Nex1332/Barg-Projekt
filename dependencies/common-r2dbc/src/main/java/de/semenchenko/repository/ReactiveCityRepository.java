package de.semenchenko.repository;

import de.semenchenko.entity.City;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ReactiveCityRepository extends R2dbcRepository<City, Long> {

}
