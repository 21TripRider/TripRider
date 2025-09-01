package com.TripRider.TripRider.service.mypage;

import com.TripRider.TripRider.domain.user.User;
import com.TripRider.TripRider.dto.auth.AddUserRequest;
import com.TripRider.TripRider.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean save(AddUserRequest dto) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return false; // 중복 이메일 존재
        }

        // 닉네임이 없으면 기본값 "익명"
        String nickname = (dto.getNickname() != null && !dto.getNickname().isBlank())
                ? dto.getNickname() : "익명";

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(nickname) // 🔹 닉네임 저장
                .build();

        userRepository.save(user);
        return true;
    }
}