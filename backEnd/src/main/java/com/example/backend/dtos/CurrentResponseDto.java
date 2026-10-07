package com.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentResponseDto {
    private String time;
    private Integer interval;
    private Double temperature_2m;
    private Double relative_humidity_2m;
    private Double apparent_temperature;
    private Double precipitation;
    private Double wind_speed_10m;
    private Integer weather_code;
}