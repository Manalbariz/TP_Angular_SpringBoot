package com.example.backend.services;

import com.example.backend.dtos.CityResponseDto;
import com.example.backend.dtos.GeocodingResponseDto;
import com.example.backend.exceptions.CityNotFoundException;
import com.example.backend.exceptions.WeatherApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CitiesService {

    private final RestClient restClient;

    @Value("${openmeteo.geocoding-url}")
    private String geocodingUrl;

    public CitiesService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public GeocodingResponseDto searchCities(String name, int count) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de la ville est obligatoire.");
        }

        URI uri = UriComponentsBuilder.fromUriString(geocodingUrl)
                .queryParam("name", name.trim())
                .queryParam("count", count)
                .queryParam("language", "fr")
                .queryParam("format", "json")
                .encode()
                .build()
                .toUri();

        try {
            GeocodingResponseDto response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GeocodingResponseDto.class);

            if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                throw new CityNotFoundException(name.trim());
            }
            return response;
        } catch (CityNotFoundException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new WeatherApiException("Erreur lors de la recherche de ville.", ex);
        }
    }

    public CityResponseDto findFirstCity(String name) {
        return searchCities(name, 1).getResults().getFirst();
    }
}
