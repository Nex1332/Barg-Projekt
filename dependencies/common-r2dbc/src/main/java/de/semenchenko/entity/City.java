package de.semenchenko.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table("weather_data.cities")
public class City {
    @Id
    @Column("city_id")
    private Long cityId;
    @Column("city_name")
    private String cityName;
}
