package com.TripRider.TripRider.service;

import com.TripRider.TripRider.dto.common.NearbyPlaceDto;
import com.TripRider.TripRider.dto.riding.RidingCourseDetailDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TourApiService {

    @Value("${tourapi.base-url}")  private String baseUrl;
    @Value("${tourapi.service-key}") private String serviceKey; // URL-encoded
    @Value("${tourapi.mobile-os:ETC}") private String mobileOs;
    @Value("${tourapi.mobile-app:TripRider}") private String mobileApp;

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate rest = new RestTemplate();

    /** 위치기반 목록(한 지점) */
    public List<NearbyPlaceDto> locationBasedList(
            double lat, double lng, int radiusMeters, int contentTypeId, int size, int page) {

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/locationBasedList2")
                .queryParam("serviceKey", serviceKey == null ? "" : serviceKey.trim()) // 원문키 그대로 넣고
                .queryParam("MobileOS", mobileOs)
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .queryParam("mapX", String.valueOf(lng))
                .queryParam("mapY", String.valueOf(lat))
                .queryParam("radius", radiusMeters)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("arrange", "E")
                .queryParam("numOfRows", size)
                .queryParam("pageNo", page)
                .build()   // <<< 여기! true 제거 (UriComponentsBuilder가 자동 인코딩)
                .toUri();



        String raw = rest.getForObject(uri, String.class);
        try {
            JsonNode items = mapper.readTree(raw)
                    .path("response").path("body").path("items").path("item");
            List<NearbyPlaceDto> list = new ArrayList<>();
            if (items.isArray()) {
                for (JsonNode n : items) list.add(toDto(n));
            }
            return list;
        } catch (Exception e) {
            return List.of();
        }
    }

    private NearbyPlaceDto toDto(JsonNode n) {
        Long contentId = n.has("contentid") ? n.get("contentid").asLong() : null;
        String title = n.path("title").asText("");
        String addr = n.path("addr1").asText("");
        String tel = n.path("tel").asText("");
        String image = n.path("firstimage").asText(null);
        double mapx = n.path("mapx").asDouble(); // 경도
        double mapy = n.path("mapy").asDouble(); // 위도
        Integer dist = n.has("dist") ? safeInt(n.get("dist").asText(null)) : null;
        Integer ctype = n.has("contenttypeid") ? n.get("contenttypeid").asInt() : null;
        return NearbyPlaceDto.builder()
                .contentId(contentId != null && contentId == 0 ? null : contentId)
                .title(title).addr(addr).tel(tel).image(image)
                .lng(mapx).lat(mapy).distMeters(dist).contentTypeId(ctype)
                .build();
    }

    private Integer safeInt(String s) {
        try { return s == null ? null : Integer.parseInt(s); } catch (Exception e) { return null; }
    }

    /** 여러 지점 결과를 합쳐서(중복 제거 + 거리 오름차순) 반환 */
    public List<NearbyPlaceDto> mergedByPoints(
            List<RidingCourseDetailDto.LatLng> points,
            int radiusMeters, int contentTypeId,
            int sizePerPoint, int maxTotal) {

        Map<String, NearbyPlaceDto> dedup = new LinkedHashMap<>();
        for (var p : points) {
            var list = locationBasedList(p.getLat(), p.getLng(), radiusMeters, contentTypeId, sizePerPoint, 1);
            for (var e : list) {
                String key;
                if (e.getContentId() != null) key = "id:" + e.getContentId();
                else key = "t:" + (e.getTitle()==null?"":e.getTitle())
                        + "|" + Math.round(e.getLat()*1e4) + "|" + Math.round(e.getLng()*1e4); // ~10m
                if (!dedup.containsKey(key) ||
                        (e.getDistMeters()!=null &&
                                (dedup.get(key).getDistMeters()==null ||
                                        e.getDistMeters() < dedup.get(key).getDistMeters()))) {
                    dedup.put(key, e);
                }
            }
        }
        List<NearbyPlaceDto> merged = new ArrayList<>(dedup.values());
        merged.sort(Comparator.comparing(x -> x.getDistMeters()==null ? Integer.MAX_VALUE : x.getDistMeters()));
        if (maxTotal > 0 && merged.size() > maxTotal) return merged.subList(0, maxTotal);
        return merged;
    }
}
