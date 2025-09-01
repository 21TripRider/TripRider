package com.TripRider.TripRider.repository.travel;

import com.TripRider.TripRider.domain.travel.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, String> {

    // userId 소유 코스 최신순
    Page<CourseEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 비로그인/게스트(또는 userId=null) 저장분을 보고 싶다면 아래도 추가 가능
    Page<CourseEntity> findByUserIdIsNullOrderByCreatedAtDesc(Pageable pageable);
}
