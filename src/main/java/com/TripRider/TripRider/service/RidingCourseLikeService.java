package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.RidingCourseLike;
import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.repository.RidingCourseLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RidingCourseLikeService {

    private final RidingCourseLikeRepository repo;

    @Transactional
    public int like(String category, Long courseId, User me) {
        if (!repo.existsByCategoryAndCourseIdAndUser(category, courseId, me)) {
            repo.save(RidingCourseLike.builder()
                    .category(category)
                    .courseId(courseId)
                    .user(me)
                    .build());
        }
        return (int) repo.countByCategoryAndCourseId(category, courseId);
    }

    @Transactional
    public int unlike(String category, Long courseId, User me) {
        if (repo.existsByCategoryAndCourseIdAndUser(category, courseId, me)) {
            repo.deleteByCategoryAndCourseIdAndUser(category, courseId, me);
        }
        return (int) repo.countByCategoryAndCourseId(category, courseId);
    }

    @Transactional(readOnly = true)
    public int count(String category, Long courseId) {
        return (int) repo.countByCategoryAndCourseId(category, courseId);
    }

    @Transactional(readOnly = true)
    public boolean likedByMe(String category, Long courseId, User me) {
        return repo.existsByCategoryAndCourseIdAndUser(category, courseId, me);
    }
}
