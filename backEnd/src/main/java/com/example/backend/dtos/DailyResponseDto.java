package com.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyResponseDto {
    private List<String> time;
    private List<Integer> weather_code;
    private List<Double> temperature_2m_max;
    private List<Double> temperature_2m_min;
    private List<Integer> precipitation_probability_max;
}
