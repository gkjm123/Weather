package com.example.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
