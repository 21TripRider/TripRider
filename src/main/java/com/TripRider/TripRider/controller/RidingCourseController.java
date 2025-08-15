package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.RidingCourseCardDto;
import com.TripRider.TripRider.dto.RidingCourseDetailDto;
import com.TripRider.TripRider.service.CourseFileService;
import com.TripRider.TripRider.service.RidingCourseLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/travel/riding")
@RequiredArgsConstructor
public class RidingCourseController {

    private final CourseFileService courseFileService;
    private final RidingCourseLikeService likeService;

    /** 카드 목록 (내 위치 없으면 거리 null) */
    @GetMapping("/cards")
    public List<RidingCourseCardDto> cards(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @AuthenticationPrincipal User me
    ) {
        var cards = courseFileService.listCards(lat, lng);
        // 좋아요 메타 주입
        return cards.stream().map(c -> {
            c.setLikeCount(likeService.count(c.getCategory(), c.getId()));
            c.setLiked(me != null && likeService.likedByMe(c.getCategory(), c.getId(), me));
            return c;
        }).toList();
    }

    /** 인기순: 좋아요 수 내림차순 */
    @GetMapping("/popular")
    public List<RidingCourseCardDto> popular(
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal User me
    ) {
        return courseFileService.listCards(null, null).stream()
                .peek(c -> {
                    c.setLikeCount(likeService.count(c.getCategory(), c.getId()));
                    c.setLiked(me != null && likeService.likedByMe(c.getCategory(), c.getId(), me));
                })
                .sorted(Comparator.comparing(
                        (RidingCourseCardDto c) -> c.getLikeCount() == null ? 0 : c.getLikeCount()
                ).reversed())
                .limit(limit)
                .toList();
    }

    /** 거리순(=코스 총 길이 기준) 정렬 */
    @GetMapping("/by-length")
    public List<RidingCourseCardDto> byLength(
            @RequestParam(defaultValue = "asc") String order,
            @AuthenticationPrincipal User me
    ) {
        var stream = courseFileService.listCards(null, null).stream()
                .peek(c -> {
                    c.setLikeCount(likeService.count(c.getCategory(), c.getId()));
                    c.setLiked(me != null && likeService.likedByMe(c.getCategory(), c.getId(), me));
                });
        Comparator<RidingCourseCardDto> cmp = Comparator.comparingInt(RidingCourseCardDto::getTotalDistanceMeters);
        if ("desc".equalsIgnoreCase(order)) cmp = cmp.reversed();
        return stream.sorted(cmp).toList();
    }

    /** 상세: category + id (좋아요 메타 포함) */
    @GetMapping("/{category}/{id}")
    public RidingCourseDetailDto detail(
            @PathVariable String category,
            @PathVariable Long id,
            @AuthenticationPrincipal User me
    ) {
        var dto = courseFileService.get(category, id);
        // 좋아요 메타 주입
        dto.setLikeCount(likeService.count(category, id));
        dto.setLiked(me != null && likeService.likedByMe(category, id, me));
        return dto;
    }

    /** 좋아요 */
    @PostMapping("/{category}/{id}/likes")
    public Map<String, Object> like(
            @PathVariable String category,
            @PathVariable Long id,
            @AuthenticationPrincipal User me
    ) {
        if (me == null) throw new RuntimeException("로그인이 필요합니다.");
        int cnt = likeService.like(category, id, me);
        return Map.of("likeCount", cnt, "liked", true);
    }

    /** 좋아요 취소 */
    @DeleteMapping("/{category}/{id}/likes")
    public Map<String, Object> unlike(
            @PathVariable String category,
            @PathVariable Long id,
            @AuthenticationPrincipal User me
    ) {
        if (me == null) throw new RuntimeException("로그인이 필요합니다.");
        int cnt = likeService.unlike(category, id, me);
        return Map.of("likeCount", cnt, "liked", false);
    }
}
