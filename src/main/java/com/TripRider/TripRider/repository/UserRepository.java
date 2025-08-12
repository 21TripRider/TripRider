package com.TripRider.TripRider.repository;

import com.TripRider.TripRider.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname); // 닉네임 중복 체크용
}