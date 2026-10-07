package com.example.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecastDto {
    private String date;
    private Integer weatherCode;
    private Double temperatureMax;
    private Double temperatureMin;
    private Integer precipitationProbability;
}
