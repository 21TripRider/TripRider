package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.SimpleWeatherResponse;
import com.TripRider.TripRider.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    // ✅ 제주시 전체 날씨 (기온, 풍속 등 여러 개)
    @GetMapping("/api/jeju-weather")
    public Map<String, List<SimpleWeatherResponse>> getJejuWeather() {
        Map<String, List<SimpleWeatherResponse>> map = new HashMap<>();
        map.put("제주시", weatherService.getJejuCityWeather());
        return map;
    }

    // ✅ 제주시 요약 날씨 (기온, 강수확률만)
    @GetMapping("/jeju-weather")
    public Map<String, String> getJejuSimpleWeather() {
        return weatherService.getSimpleWeather("제주시");
    }
}
