package com.TripRider.TripRider.preset;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

public class PresetResolver {

    @Value @Builder
    public static class Filter {
        int contentTypeId;     // 12/14/15/28/32/38/39
        String cat1;           // nullable
        String cat2;           // nullable
        String cat3;           // nullable
        String keyword;        // nullable (폴백으로 searchKeyword2 호출)
    }

    public static final Map<String, Filter> PRESETS = Map.ofEntries(
            // 1) 관광지(12)
            entry("tour.nature",     12, "A01", null, null, null),          // 자연
            entry("tour.history",    12, "A02", null, null, "역사"),        // 역사(키워드 보강)
            entry("tour.experience", 12, "A02", null, null, "체험"),        // 체험(키워드 보강)

            // 2) 음식점(39)
            entry("food.korean",     39, "A05", "A0502", null, null),       // 한식
            entry("food.chinese",    39, "A05", "A0503", null, null),       // 중식
            entry("food.japanese",   39, "A05", "A0504", null, null),       // 일식
            entry("food.western",    39, "A05", "A0505", null, null),       // 양식
            entry("food.etc",        39, "A05", "A0508", null, null),       // 기타

            // 3) 레포츠(28)
            entry("leports.land",    28, "A03", "A0302", null, null),       // 육상
            entry("leports.water",   28, "A03", "A0303", null, null),       // 수상
            entry("leports.air",     28, "A03", "A0304", null, null),       // 항공
            entry("leports.complex", 28, "A03", "A0305", null, null),       // 복합

            // 4) 문화시설(14)
            entry("culture.museum",   14, "A02", "A0206", "A02060100", null),  // 박물관
            entry("culture.memorial", 14, "A02", "A0206", "A02060200", null),  // 기념관
            entry("culture.artmuseum",14, "A02", "A0206", "A02060500", null),  // 미술관

            // 5) 축제/행사(15) - 기관 세부코드 변동 가능성 대비 키워드 폴백
            entry("event.performance", 15, "A02", null, null, "공연"),
            entry("event.exhibition",  15, "A02", null, null, "전시회"),
            entry("event.expo",        15, "A02", null, null, "박람회"),

            // 7) 쇼핑(38) - 지역 편차 대비 키워드 병행
            entry("shop.department",   38, "A04", null, null, "백화점"),
            entry("shop.hypermart",    38, "A04", null, null, "대형마트"),
            entry("shop.traditional",  38, "A04", null, null, "전통시장")
    );

    private static Map.Entry<String, Filter> entry(
            String k, int type, String c1, String c2, String c3, String kw) {
        return Map.entry(k, Filter.builder()
                .contentTypeId(type).cat1(c1).cat2(c2).cat3(c3).keyword(kw).build());
    }

    public static Filter of(String key) {
        Filter f = PRESETS.get(key);
        if (f == null) throw new IllegalArgumentException("Unknown preset: " + key);
        return f;
    }
}