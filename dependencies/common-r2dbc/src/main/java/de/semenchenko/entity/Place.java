package de.semenchenko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("weather_data.places")
public class Place {
    @Id
    private Long placeId;
    private String placeName;
    private String road;
    private String suburb;
    private String city;
    private String country;

    @Column("city_Id")
    private Long cityId;
}
