package de.semenchenko;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WeatherServiceTest {
    @Value("${service.weather.uri}")
    private String weatherAPIUri;
    private final WebClient weatherAPIWebClient;

    public WeatherServiceTest() {
        this.weatherAPIWebClient = WebClient.create("https://api.openweathermap.org/data/2.5");
    }


    @Test
    void testIsCityValid() {
        Mono<Boolean> result = weatherAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", "dasd")
                        .queryParam("appid", "c3aaabcddb63080b0079bd7df9815848")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> true)
                .onErrorResume(e -> Mono.just(false));

        // Блокируем выполнение и получаем результат
        Boolean isValid = result.block();
        System.out.println("Is the city valid? " + isValid);
    }

    @Test
    void testWeatherApi() {
        String city = "пывпы"; // Подставьте нужный город
        String apiKey = "c3aaabcddb63080b0079bd7df9815848";

        Mono<String> responseMono = weatherAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        responseMono.subscribe(
                response -> System.out.println("Response from API: " + response),
                error -> System.err.println("Error occurred: " + error.getMessage() + city)
        );

        // Дождитесь завершения асинхронной операции (для консольного приложения)
        try {
            Thread.sleep(3000); // Время ожидания, чтобы получить ответ
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
