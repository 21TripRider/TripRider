package com.TripRider.TripRider.repository;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.domain.UserBadge;
import com.TripRider.TripRider.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUser(User user);
    boolean existsByUserAndBadge(User user, Badge badge);
}
