package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.NearbyPlaceDto;
import com.TripRider.TripRider.dto.RidingCourseDetailDto;
import com.TripRider.TripRider.service.CourseFileService;
import com.TripRider.TripRider.service.TourApiService;
import com.TripRider.TripRider.util.CoursePointsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/nearby")
public class NearbyController {

    private final CourseFileService courseFileService;
    private final TourApiService tourApiService;

    // 카테고리 코드: 관광지/문화/행사/레포츠/숙박/쇼핑/음식점
    private static final Map<String, Integer> TYPE = Map.of(
            "tour", 12, "culture", 14, "event", 15,
            "leports", 28, "stay", 32, "shop", 38, "food", 39
    );

    /**
     * 코스 기준으로 주변 장소 조회
     * mode=sme  : 시작·중간(경로길이 50%)·끝 3지점
     * mode=along: 경로를 count개로 균등 분할하여 샘플
     */
    @GetMapping("/{category}/{id}")
    public Map<String, List<NearbyPlaceDto>> nearbyByCourse(
            @PathVariable String category,
            @PathVariable Long id,
            @RequestParam(defaultValue = "3000") int radius,     // m
            @RequestParam(defaultValue = "8") int size,          // 지점당 가져올 개수
            @RequestParam(defaultValue = "sme") String mode,     // sme | along
            @RequestParam(defaultValue = "5") int count          // mode=along 일 때 샘플 개수
    ) {
        RidingCourseDetailDto course = courseFileService.get(category, id);

        // 검색 포인트 결정
        List<RidingCourseDetailDto.LatLng> points =
                "along".equalsIgnoreCase(mode)
                        ? CoursePointsUtil.sampleByCount(course, Math.max(2, count))
                        : CoursePointsUtil.startMidEndByLength(course);

        // 총 반환 상한(지점수 * 지점당 size의 1.2배 정도)
        int maxTotal = Math.max(10, (int) Math.round(points.size() * size * 1.2));

        Map<String, List<NearbyPlaceDto>> result = new LinkedHashMap<>();
        TYPE.forEach((key, ctype) -> {
            List<NearbyPlaceDto> merged = tourApiService.mergedByPoints(
                    points, radius, ctype, size, maxTotal
            );
            result.put(key, merged);
        });
        return result;
    }

    /**
     * 임의 좌표 기준 주변 장소 조회 (지도 중심 재검색 등)
     * 카테고리별로 한 번에 내려줌
     */
    @GetMapping("/point")
    public Map<String, List<NearbyPlaceDto>> nearbyByPoint(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "3000") int radius,
            @RequestParam(defaultValue = "8") int size
    ) {
        var singlePoint = List.of(new RidingCourseDetailDto.LatLng(lat, lng));
        int maxTotal = size * TYPE.size();

        Map<String, List<NearbyPlaceDto>> result = new LinkedHashMap<>();
        TYPE.forEach((key, ctype) -> {
            List<NearbyPlaceDto> merged = tourApiService.mergedByPoints(
                    singlePoint, radius, ctype, size, maxTotal
            );
            result.put(key, merged);
        });
        return result;
    }
}
