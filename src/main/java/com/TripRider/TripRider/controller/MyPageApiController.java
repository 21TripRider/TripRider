
package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.MyPageResponse;
import com.TripRider.TripRider.dto.MyPageUpdateRequest;
import com.TripRider.TripRider.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageApiController {

    private final MyPageService myPageService;

    // 마이페이지 정보 조회
    @GetMapping
    public ResponseEntity<MyPageResponse> getMyPage(@AuthenticationPrincipal User user) {
        MyPageResponse response = MyPageResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .badge(user.getBadge())
                .profileImage(user.getProfileImage())
                .totalDistance(user.getTotalDistance())
                .build();
        return ResponseEntity.ok(response);
    }

    // 마이페이지 정보 수정
    @PutMapping
    public ResponseEntity<?> updateMyPage(@RequestBody MyPageUpdateRequest request,
                                          @AuthenticationPrincipal User user) {
        myPageService.updateProfile(request.getNickname(), request.getIntro(), request.getBadge(), user);
        return ResponseEntity.ok("마이페이지 수정 완료");
    }

    // 한줄 소개만 수정
    @PutMapping("/intro")
    public ResponseEntity<?> updateIntro(@RequestBody String intro,
                                         @AuthenticationPrincipal User user) {
        myPageService.updateIntro(intro, user);
        return ResponseEntity.ok("한줄소개 수정 완료");
    }

    // 프로필 이미지 업로드
    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile image,
                                                     @AuthenticationPrincipal User user) {
        String url = myPageService.updateProfileImage(user, image);
        return ResponseEntity.ok(url);
    }
}
