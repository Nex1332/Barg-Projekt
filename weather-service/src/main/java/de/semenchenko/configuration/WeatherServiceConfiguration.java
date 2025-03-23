package de.semenchenko.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
public class WeatherServiceConfiguration {
    @Value("${service.weather.uri}")
    private String weatherAPIUri;
    @Value("${service.overpass.uri}")
    private String overpassAPI;

    @Bean
    public WebClient weatherAPIWebClient() {
        return WebClient.builder()
                .baseUrl(weatherAPIUri)
                .build();
    }

    @Bean
    public WebClient overpassAPIWebClient() {
        return WebClient.builder()
                .baseUrl(overpassAPI)
                .build();
    }

    @Bean
    public Map<String, Map<String, String>> overpassTags() {
        return Map.of(
                "volleyball", Map.of("leisure", "pitch", "sport", "beach_volleyball"),
                "soccer", Map.of("leisure", "pitch", "sport", "soccer"),
                "basketball", Map.of("leisure", "pitch", "sport", "basketball")
        );
    }
}
