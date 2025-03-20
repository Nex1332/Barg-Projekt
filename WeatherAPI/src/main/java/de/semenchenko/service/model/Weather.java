package de.semenchenko.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private String weatherCondition;
    private LocalDate date;
    private String city;
}
