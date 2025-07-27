package com.TripRider.TripRider.service;

import com.TripRider.TripRider.dto.SimpleWeatherResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Map<String, int[]> JEJU_LOCATIONS = Map.of(
            "제주시", new int[]{53, 38},
            "서귀포시", new int[]{52, 33},
            "애월읍", new int[]{51, 39},
            "성산읍", new int[]{60, 47},
            "한림읍", new int[]{48, 38},
            "표선면", new int[]{58, 39},
            "조천읍", new int[]{55, 42},
            "남원읍", new int[]{56, 36},
            "구좌읍", new int[]{58, 43}
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

    public Map<String, List<SimpleWeatherResponse>> getAllJejuWeather() {
        String[] dateTime = getLatestBaseDateTime();
        String baseDate = dateTime[0];
        String baseTime = dateTime[1];

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<SimpleWeatherResponse>> resultMap = new HashMap<>();

        for (Map.Entry<String, int[]> entry : JEJU_LOCATIONS.entrySet()) {
            String regionName = entry.getKey();
            int[] coords = entry.getValue();
            String url = baseUrl + "/getVilageFcst" +
                    "?serviceKey=" + serviceKey +
                    "&pageNo=1&numOfRows=1000&dataType=JSON" +
                    "&base_date=" + baseDate +
                    "&base_time=" + baseTime +
                    "&nx=" + coords[0] +
                    "&ny=" + coords[1];

            try {
                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = mapper.readTree(response);
                JsonNode items = root.path("response").path("body").path("items").path("item");

                List<SimpleWeatherResponse> weatherList = new ArrayList<>();

                for (JsonNode item : items) {
                    String category = item.path("category").asText();
                    if (REQUIRED_CATEGORIES.contains(category)) {
                        SimpleWeatherResponse simple = new SimpleWeatherResponse();
                        simple.setCategory(CATEGORY_MAP.get(category)); // ✅ 한글로 바꿔서 세팅
                        simple.setFcstTime(item.path("fcstTime").asText());
                        simple.setFcstValue(item.path("fcstValue").asText());
                        weatherList.add(simple);
                    }
                }

                resultMap.put(regionName, weatherList);

            } catch (Exception e) {
                log.warn("[" + regionName + "] 날씨 정보 조회 실패: {}", e.getMessage());
                resultMap.put(regionName, List.of());
            }
        }

        return resultMap;
    }

    /*
    기온, 강수확률 메서드
     */
    public Map<String, String> getSimpleWeather(String region) {
        Map<String, List<SimpleWeatherResponse>> allWeather = getAllJejuWeather();
        List<SimpleWeatherResponse> regionWeather = allWeather.getOrDefault(region, List.of());

        String temp = null;
        String rain = null;

        for (SimpleWeatherResponse response : regionWeather) {
            if ("기온".equals(response.getCategory())) {
                temp = response.getFcstValue();
            } else if ("강수확률".equals(response.getCategory())) {
                rain = response.getFcstValue();
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("temp", temp);
        result.put("rain", rain);
        return result;
    }

}
