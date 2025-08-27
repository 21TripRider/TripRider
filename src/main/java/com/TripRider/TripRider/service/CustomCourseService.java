package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.CourseEntity;
import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.custom.*;
import com.TripRider.TripRider.repository.CourseRepository;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomCourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final BadgeService badgeService; // ✅ 추가

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

        // ✅ 거리 누적 + 뱃지 지급
        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow();
            int km = Optional.ofNullable(req.getDistanceKm()).orElse(0.0).intValue();
            user.setTotalDistance(user.getTotalDistance() + km);

            badgeService.checkAndGrantDistanceBadges(user);
            userRepository.save(user);
        }

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
        e.getWaypoints().size(); // LAZY 초기화
        return CourseMapper.toView(e);
    }

    @Transactional
    public void delete(String id, Long userId) {
        courseRepository.deleteById(id);
    }
}
