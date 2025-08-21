package com.TripRider.TripRider.service;

import com.TripRider.TripRider.dto.common.NearbyPlaceDto;
import com.TripRider.TripRider.dto.custom.PlacePageDto;
import com.TripRider.TripRider.preset.PresetResolver;
import com.TripRider.TripRider.service.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresetPlaceService {

    private final TourApiService tour;

    /**
     * 프리셋 키 기반 제주 전역 검색
     * 1) areaBasedList2(cat 조합) 호출
     * 2) 결과가 없고 keyword가 있으면 searchKeyword2로 폴백
     */
    public PlacePageDto searchByPreset(String presetKey, int page, int limit) {
        PresetResolver.Filter f = PresetResolver.of(presetKey);

        // 1) 제주 전역(39) + 카테고리
        List<NearbyPlaceDto> items = tour.areaBasedList(
                39, null, f.getContentTypeId(),
                f.getCat1(), f.getCat2(), f.getCat3(),
                limit, page, "B"
        );

        // 2) 폴백: 키워드 검색
        if ((items == null || items.isEmpty()) && f.getKeyword() != null) {
            items = tour.searchKeyword(
                    f.getKeyword(), 39, null,
                    f.getContentTypeId(), limit, page, "B"
            );
        }

        if (items == null) items = new ArrayList<>();
        return PlacePageDto.builder()
                .items(items)
                .page(page)
                .total(-1)
                .build();
    }
}
