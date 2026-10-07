package com.example.backend.exceptions;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String city) {
        super("Aucune ville trouvée pour « " + city + " ».");
    }
}
