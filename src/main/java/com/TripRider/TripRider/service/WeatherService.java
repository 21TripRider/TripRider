package com.TripRider.TripRider.service;

import com.TripRider.TripRider.dto.weather.SimpleWeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.service-key}")
    private String serviceKey;

    // ✅ 영어 → 한글 매핑
    private static final Map<String, String> CATEGORY_MAP = Map.of(
            "TMP", "기온",
            "WSD", "풍속",
            "SKY", "하늘 상태",
            "PTY", "강수 형태",
            "POP", "강수확률",
            "PCP", "강수량",
            "SNO", "적설량"
    );

    private static final List<String> REQUIRED_CATEGORIES = new ArrayList<>(CATEGORY_MAP.keySet());

    // ✅ 제주시만 사용
    private static final Map<String, int[]> JEJU_LOCATIONS = Map.of(
            "제주시", new int[]{53, 38}
    );

    private String[] getLatestBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int hour = now.getHour();

        String baseTime;
        if (hour >= 23) baseTime = "2300";
        else if (hour >= 20) baseTime = "2000";
        else if (hour >= 17) baseTime = "1700";
        else if (hour >= 14) baseTime = "1400";
        else if (hour >= 11) baseTime = "1100";
        else if (hour >= 8) baseTime = "0800";
        else if (hour >= 5) baseTime = "0500";
        else baseTime = "0200";

        return new String[]{baseDate, baseTime};
    }

    // ✅ 제주시 전체 날씨 가져오기 (기온, 강수, 풍속 등)
    public List<SimpleWeatherResponse> getJejuCityWeather() {
        String[] dateTime = getLatestBaseDateTime();
        String baseDate = dateTime[0];
        String baseTime = dateTime[1];

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        int[] coords = JEJU_LOCATIONS.get("제주시");
        String url = baseUrl + "/getVilageFcst" +
                "?serviceKey=" + serviceKey +
                "&pageNo=1&numOfRows=1000&dataType=JSON" +
                "&base_date=" + baseDate +
                "&base_time=" + baseTime +
                "&nx=" + coords[0] +
                "&ny=" + coords[1];

        List<SimpleWeatherResponse> weatherList = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                if (REQUIRED_CATEGORIES.contains(category)) {
                    SimpleWeatherResponse simple = new SimpleWeatherResponse();
                    simple.setCategory(CATEGORY_MAP.get(category));
                    simple.setFcstTime(item.path("fcstTime").asText());
                    simple.setFcstValue(item.path("fcstValue").asText());
                    weatherList.add(simple);
                }
            }

        } catch (Exception e) {
            log.warn("[제주시] 날씨 정보 조회 실패: {}", e.getMessage());
        }

        return weatherList;
    }

    // ✅ 기온 + 강수확률 요약 조회 (null 방지)
    public Map<String, String> getSimpleWeather(String region) {
        List<SimpleWeatherResponse> weatherList;

        if ("제주시".equals(region)) {
            weatherList = getJejuCityWeather();
        } else {
            weatherList = List.of();
        }

        // 현재 시각 기준 가장 가까운 예보 시간 (기상청은 3시간 단위 예보)
        String currentTime = LocalDateTime.now().plusHours(1)
                .format(DateTimeFormatter.ofPattern("HH00")); // 예: 0930 → 1000

        String temp = null;
        String rain = null;

        for (SimpleWeatherResponse response : weatherList) {
            if (!response.getFcstTime().equals(currentTime)) continue;

            if ("기온".equals(response.getCategory())) {
                temp = response.getFcstValue();
            } else if ("강수확률".equals(response.getCategory())) {
                rain = response.getFcstValue();
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("temp", temp != null ? temp : "N/A");
        result.put("rain", rain != null ? rain : "N/A");

        return result;
    }
}
