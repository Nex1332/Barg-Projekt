package de.semenchenko.service.impl;

import de.semenchenko.PlaceDTO;
import de.semenchenko.entity.Place;
import de.semenchenko.repository.ReactivePlaceRepository;
import de.semenchenko.service.PlaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceServiceImpl implements PlaceService {
    private final ReactivePlaceRepository reactivePlaceRepository;
    private final Map<String, Map<String, String>>  tags;
    private final WebClient overpassAPIWebClient;

    public PlaceServiceImpl(ReactivePlaceRepository reactivePlaceRepository, Map<String, Map<String, String>>  tags, WebClient overpassAPIWebClient) {
        this.reactivePlaceRepository = reactivePlaceRepository;
        this.tags = tags;
        this.overpassAPIWebClient = overpassAPIWebClient;
    }

    @Override
    public Mono<Void> updateTablePlace(String city) {
        return Flux.fromIterable(tags.entrySet())
                .flatMap(entry -> getPlaces(city, entry.getValue()))
                .flatMap()
    }

    private Flux<PlaceDTO> getPlaces(String city, Map<String, String> tags) {
        String query = buildQuery(city, tags);

        return overpassAPIWebClient.post()
                .bodyValue("data=" + query)
                .retrieve()
                .bodyToFlux(PlaceDTO.class);
    }

    private String buildQuery(String city, Map<String, String> tags) {
        StringBuilder query = new StringBuilder("[out:json];\n");
        query.append(String.format("area[name=\"%s\"]->.searchArea;\n", city));
        query.append("nwr");

        tags.forEach((key, value) -> query.append(String.format("[\"%s\"=\"%s\"]", key, value)));

        query.append("(area.searchArea);\nout center;");
        return query.toString();
    }
}
