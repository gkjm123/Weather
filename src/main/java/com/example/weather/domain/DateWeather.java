package com.example.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "DateWeather")
public class DateWeather {
    @Id
    private LocalDate date;
    private String weather;
    private String icon;
    private Double temperature;

}
