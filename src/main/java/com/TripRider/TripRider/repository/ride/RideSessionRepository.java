package com.TripRider.TripRider.repository.ride;

import com.TripRider.TripRider.domain.ride.RideSession;
import com.TripRider.TripRider.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideSessionRepository extends JpaRepository<RideSession, Long> {
    List<RideSession> findByUserOrderByCreatedAtDesc(User user);
}