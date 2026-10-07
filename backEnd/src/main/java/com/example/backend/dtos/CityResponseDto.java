package com.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityResponseDto {
    private String name;
    private Double latitude;
    private Double longitude;
    private String country;
    private String admin1;
}
