package de.semenchenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Table("weather_data.weather")
public class Weather {
    @Id
    private Long weatherId;
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private String weatherCondition;
    private LocalDate date;

    @Column
    private Long cityId;
}
