package de.semenchenko.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private double pressure;
    private double humidity;
    private double sea_level;
    private double ground_level;

    public String getWeatherDescription() {
        return String.format(
                "Current weather: \n" +
                        "- Temperature: %.2f°C\n" +
                        "- Feels like: %.2f°C\n" +
                        "- Minimum temperature: %.2f°C\n" +
                        "- Maximum temperature: %.2f°C\n" +
                        "- Pressure: %.2f hPa\n" +
                        "- Humidity: %.2f%%\n" +
                        "- Sea level: %.2f hPa\n" +
                        "- Ground level: %.2f hPa",
                temp, feels_like, temp_min, temp_max, pressure, humidity, sea_level, ground_level
        );
    }
}
