package com.example.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherViewDto {
    private String cityName;
    private String country;
    private String admin1;
    private Double latitude;
    private Double longitude;
    private String timezone;
    private String time;
    private Double temperature;
    private Double apparentTemperature;
    private Double humidity;
    private Double precipitation;
    private Double windSpeed;
    private Integer weatherCode;
    private List<DailyForecastDto> daily;
}
