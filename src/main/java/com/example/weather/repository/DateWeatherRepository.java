package com.example.weather.repository;

import com.example.weather.domain.DateWeather;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {

  List<DateWeather> findAllByDate(LocalDate date);
}
