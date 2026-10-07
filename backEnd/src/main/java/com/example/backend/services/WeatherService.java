package com.example.backend.services;

import com.example.backend.dtos.CityResponseDto;
import com.example.backend.dtos.CurrentResponseDto;
import com.example.backend.dtos.DailyForecastDto;
import com.example.backend.dtos.DailyResponseDto;
import com.example.backend.dtos.WeatherRequestDto;
import com.example.backend.dtos.WeatherResponseDto;
import com.example.backend.dtos.WeatherViewDto;
import com.example.backend.exceptions.WeatherApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final String CURRENT_FIELDS =
            "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,wind_speed_10m,weather_code";
    private static final String DAILY_FIELDS =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max";

    private final RestClient restClient;
    private final CitiesService citiesService;

    @Value("${openmeteo.weather-url}")
    private String weatherUrl;

    public WeatherService(RestClient.Builder builder, CitiesService citiesService) {
        this.restClient = builder.build();
        this.citiesService = citiesService;
    }

    public WeatherViewDto getWeatherByCity(String city) {
        CityResponseDto cityResult = citiesService.findFirstCity(city);
        WeatherRequestDto request = new WeatherRequestDto();
        request.setLatitude(cityResult.getLatitude());
        request.setLongitude(cityResult.getLongitude());
        return toView(cityResult, fetchForecast(request));
    }

    public WeatherViewDto getWeather(WeatherRequestDto weatherRequestDto) {
        CityResponseDto city = new CityResponseDto();
        city.setName(weatherRequestDto.getCityName());
        city.setCountry(weatherRequestDto.getCountry());
        city.setAdmin1(weatherRequestDto.getAdmin1());
        city.setLatitude(weatherRequestDto.getLatitude());
        city.setLongitude(weatherRequestDto.getLongitude());
        return toView(city, fetchForecast(weatherRequestDto));
    }

    private WeatherResponseDto fetchForecast(WeatherRequestDto weatherRequestDto) {
        if (weatherRequestDto.getLatitude() == null || weatherRequestDto.getLongitude() == null) {
            throw new IllegalArgumentException("La latitude et la longitude sont obligatoires.");
        }

        URI uri = UriComponentsBuilder.fromUriString(weatherUrl)
                .queryParam("latitude", weatherRequestDto.getLatitude())
                .queryParam("longitude", weatherRequestDto.getLongitude())
                .queryParam("current", CURRENT_FIELDS)
                .queryParam("daily", DAILY_FIELDS)
                .queryParam("timezone", "auto")
                .encode()
                .build()
                .toUri();

        try {
            WeatherResponseDto response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(WeatherResponseDto.class);

            if (response == null || response.getCurrent() == null) {
                throw new WeatherApiException("Réponse météo invalide.");
            }
            return response;
        } catch (WeatherApiException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new WeatherApiException("Erreur lors de la récupération de la météo.", ex);
        }
    }

    private WeatherViewDto toView(CityResponseDto city, WeatherResponseDto weather) {
        CurrentResponseDto current = weather.getCurrent();
        return WeatherViewDto.builder()
                .cityName(city.getName())
                .country(city.getCountry())
                .admin1(city.getAdmin1())
                .latitude(city.getLatitude())
                .longitude(city.getLongitude())
                .timezone(weather.getTimezone())
                .time(current.getTime())
                .temperature(current.getTemperature_2m())
                .apparentTemperature(current.getApparent_temperature())
                .humidity(current.getRelative_humidity_2m())
                .precipitation(current.getPrecipitation())
                .windSpeed(current.getWind_speed_10m())
                .weatherCode(current.getWeather_code())
                .daily(mapDaily(weather.getDaily()))
                .build();
    }

    private List<DailyForecastDto> mapDaily(DailyResponseDto daily) {
        List<DailyForecastDto> forecasts = new ArrayList<>();
        if (daily == null || daily.getTime() == null) {
            return forecasts;
        }

        int size = daily.getTime().size();
        for (int i = 0; i < size; i++) {
            forecasts.add(new DailyForecastDto(
                    daily.getTime().get(i),
                    getAt(daily.getWeather_code(), i),
                    getAt(daily.getTemperature_2m_max(), i),
                    getAt(daily.getTemperature_2m_min(), i),
                    getAt(daily.getPrecipitation_probability_max(), i)
            ));
        }
        return forecasts;
    }

    private <T> T getAt(List<T> values, int index) {
        if (values == null || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }
}
