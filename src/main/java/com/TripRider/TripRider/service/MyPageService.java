package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    // 🔹 프로필 이미지 업데이트
    public void updateProfileImage(User user, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

                // 사용자 홈 디렉토리 기준 안전한 위치로 변경
                String uploadDir = System.getProperty("user.home") + "/triprider-uploads";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    if (!created) throw new RuntimeException("디렉토리 생성 실패");
                }

                String fullPath = uploadDir + "/" + filename;
                image.transferTo(new File(fullPath));

                // 저장된 경로는 웹에서 접근 가능한 상대 경로로 저장
                user.updateProfileImage("/uploads/" + filename);
                userRepository.save(user);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        } else {
            throw new IllegalArgumentException("이미지가 비어 있습니다.");
        }
    }

    // 🔹 닉네임, 한줄소개, 뱃지 업데이트
    public void updateProfile(String nickname, String intro, String badge, User user) {
        user.setNickname(nickname);
        user.setIntro(intro);
        user.setBadge(badge);
        userRepository.save(user);
    }

    // 🔹 한줄소개만 따로 업데이트
    public void updateIntro(String intro, User user) {
        user.setIntro(intro);
        userRepository.save(user);
    }
}
