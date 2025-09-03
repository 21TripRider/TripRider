package com.TripRider.TripRider.repository.user;

import com.TripRider.TripRider.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ✅ 추가: 이메일 중복 체크
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname); // 닉네임 중복 체크용
}