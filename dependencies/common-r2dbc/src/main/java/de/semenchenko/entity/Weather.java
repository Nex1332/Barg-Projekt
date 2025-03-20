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
    @Column("weather_id")
    private Long weatherId;
    @Column("temperature")
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    @Column("weather_conditions")
    private String weatherCondition;
    private LocalDate date;
    private String city;
}
