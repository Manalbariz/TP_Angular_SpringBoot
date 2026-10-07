package com.example.backend.controllers;

import com.example.backend.dtos.GeocodingResponseDto;
import com.example.backend.dtos.WeatherRequestDto;
import com.example.backend.dtos.WeatherViewDto;
import com.example.backend.services.CitiesService;
import com.example.backend.services.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final CitiesService citiesService;
    private final WeatherService weatherService;

    public WeatherController(CitiesService citiesService, WeatherService weatherService) {
        this.citiesService = citiesService;
        this.weatherService = weatherService;
    }

    @GetMapping("/cities")
    public GeocodingResponseDto searchCities(@RequestParam String name) {
        return citiesService.searchCities(name.trim(), 5);
    }

    @GetMapping("/weather")
    public WeatherViewDto getWeatherByCity(@RequestParam String city) {
        return weatherService.getWeatherByCity(city.trim());
    }

    @GetMapping("/weather/coordinates")
    public WeatherViewDto getWeatherByCoordinates(WeatherRequestDto weatherRequestDto) {
        return weatherService.getWeather(weatherRequestDto);
    }
}
