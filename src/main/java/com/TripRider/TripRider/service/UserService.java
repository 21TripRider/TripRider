package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.AddUserRequest;
import com.TripRider.TripRider.repository.UserRepository;
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

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname("익명")
                .build();

        userRepository.save(user);
        return true;
    }
}
