package de.semenchenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("weather_data.cities")
public class City {
    @Id
    private Long cityId;
    private String cityName;
}
