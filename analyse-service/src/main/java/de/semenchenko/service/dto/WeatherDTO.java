package de.semenchenko.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private String weatherCondition;
    private LocalDate date;
}
