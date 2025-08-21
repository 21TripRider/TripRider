package com.TripRider.TripRider.service;
import com.TripRider.TripRider.domain.CourseEntity;
import com.TripRider.TripRider.dto.custom.*;
import com.TripRider.TripRider.repository.CourseRepository;
import com.TripRider.TripRider.service.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomCourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseView save(SaveCourseReq req, Long userId) {
        String id = "crs_" + UUID.randomUUID().toString().replace("-", "");
        List<CoursePreview.Waypoint> wps =
                (req.getWaypoints() == null) ? new ArrayList<>() : req.getWaypoints();

        CourseEntity e = CourseMapper.toEntity(
                id,
                userId,
                Optional.ofNullable(req.getTitle()).orElse("나의 여행 코스"),
                Optional.ofNullable(req.getDistanceKm()).orElse(0.0),
                Optional.ofNullable(req.getDurationMin()).orElse(0),
                req.getPolyline(),
                wps
        );

        CourseEntity saved = courseRepository.save(e);
        return CourseMapper.toView(saved);
    }

    @Transactional(readOnly = true)
    public List<CourseCard> findMine(Long userId, int page, int size) {
        var pageable = PageRequest.of(Math.max(page-1, 0), Math.max(size, 1));
        var pageData = (userId == null)
                ? courseRepository.findByUserIdIsNullOrderByCreatedAtDesc(pageable)
                : courseRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return pageData.getContent().stream()
                .map(CourseMapper::toCard)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseView detail(String id) {
        CourseEntity e = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("course not found: " + id));
        // LAZY 컬렉션 초기화 필요시 접근
        e.getWaypoints().size();
        return CourseMapper.toView(e);
    }

    @Transactional
    public void delete(String id, Long userId) {
        // (선택) userId 검증하려면 엔티티 조회 후 소유자 확인
        courseRepository.deleteById(id);
    }
}