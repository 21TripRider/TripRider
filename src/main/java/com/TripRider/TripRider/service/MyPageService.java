package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.MyPageResponse;
import com.TripRider.TripRider.dto.MyPageUpdateRequest;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    public MyPageResponse getMyPage() {
        User user = getCurrentUser();
        MyPageResponse response = new MyPageResponse();
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());
        response.setBadge(user.getBadge());
        return response;
    }

    public void updateMyPage(MyPageUpdateRequest dto) {
        User user = getCurrentUser();
        user.updateProfile(dto.getNickname(), dto.getBadge());
        userRepository.save(user);
    }
}
