package com.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDto {
    private Double latitude;
    private Double longitude;
    private String timezone;
    private CurrentResponseDto current;
    private DailyResponseDto daily;
}