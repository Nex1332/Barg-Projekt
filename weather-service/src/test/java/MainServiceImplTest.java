import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class MainServiceImplTest {
    private String weatherAPIUri = "http://localhost:8080/weather-API";
    private WebClient weatherAPIWebClient;

    @BeforeEach
    public void setup() {
        weatherAPIWebClient = WebClient.builder()
                .baseUrl(weatherAPIUri)
                .build();
    }

    @Test
    public void processWeatherRequests() {
        weatherAPIWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/get-random-weather")
                        .queryParam("city", "Dnipro")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    System.out.println("Response: " + response);
                })
                .doOnError(error -> {
                    System.err.println("Error: " + error.getMessage());
                })
                .block();
    }
}
