package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.custom.*;
import com.TripRider.TripRider.service.CourseBuilderService;
import com.TripRider.TripRider.service.CustomCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/custom/courses")
@RequiredArgsConstructor
public class CustomCourseController {

    private final CourseBuilderService builderService;
    private final CustomCourseService courseService;

    // 바구니 기반 자동 코스 미리보기
    @PostMapping("/auto")
    public CoursePreview auto(@RequestBody AutoCourseReq req) {
        return builderService.buildFromSelection(req.getSelectionId(), req.isOptimize());
    }

    // 수동(정렬된 웨이포인트) 기반 미리보기도 필요하면 사용
    @PostMapping("/manual/preview")
    public CoursePreview manualPreview(@RequestBody CoursePreview preview,
                                       @RequestParam(defaultValue = "true") boolean optimize) {
        return builderService.buildFromManual(preview.getWaypoints(), optimize);
    }

    // 저장
    @PostMapping
    public CourseView save(@RequestBody SaveCourseReq req,
                           @RequestHeader(value="X-USER-ID", required=false) Long userId) {
        return courseService.save(req, userId);
    }

    // 내 코스 리스트
    @GetMapping("/mine")
    public List<CourseCard> mine(@RequestHeader(value="X-USER-ID", required=false) Long userId,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        return courseService.findMine(userId, page, size);
    }

    @GetMapping("/{id}")
    public CourseView detail(@PathVariable String id) { return courseService.detail(id); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id,
                       @RequestHeader(value="X-USER-ID", required=false) Long userId) {
        courseService.delete(id, userId);
    }
}