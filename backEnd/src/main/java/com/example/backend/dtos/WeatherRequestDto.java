package com.example.backend.dtos;

import lombok.Data;

@Data
public class WeatherRequestDto {
    private Double latitude;
    private Double longitude;
    private String cityName;
    private String country;
    private String admin1;
}