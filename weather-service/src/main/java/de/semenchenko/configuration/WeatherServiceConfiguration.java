package de.semenchenko.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WeatherServiceConfiguration {
    @Value("${service.weather.uri}")
    private String weatherAPIUri;

    @Bean
    public WebClient weatherAPIWebClient() {
        return WebClient.builder()
                .baseUrl(weatherAPIUri)
                .build();
    }
}
