package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.SimpleWeatherResponse;
import com.TripRider.TripRider.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/api/jeju-weather")
    public Map<String, List<SimpleWeatherResponse>> getJejuWeather() {
        return weatherService.getAllJejuWeather();
    }
}
