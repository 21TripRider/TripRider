
package com.TripRider.TripRider.controller.mypage;

import com.TripRider.TripRider.domain.user.User;
import com.TripRider.TripRider.dto.mypage.MyPageResponse;
import com.TripRider.TripRider.dto.mypage.MyPageUpdateRequest;
import com.TripRider.TripRider.dto.mypage.RepresentativeBadgeRequest;
import com.TripRider.TripRider.service.mypage.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.TripRider.TripRider.domain.user.Badge;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageApiController {

    private final MyPageService myPageService;

    // 🔹 마이페이지 정보 조회
    @GetMapping
    public ResponseEntity<MyPageResponse> getMyPage(@AuthenticationPrincipal User user) {
        String representativeBadge = user.getRepresentativeBadge();
        if (representativeBadge == null && user.getBadge() != null) {
            representativeBadge = user.getBadge(); // 최근 획득 뱃지를 기본 대표로
        }

        MyPageResponse response = MyPageResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .profileImage(user.getProfileImage())
                .totalDistance(user.getTotalDistance())
                .representativeBadge(representativeBadge)
                .build();

        return ResponseEntity.ok(response);
    }

    // 🔹 마이페이지 정보 수정
    @PutMapping
    public ResponseEntity<?> updateMyPage(@RequestBody MyPageUpdateRequest request,
                                          @AuthenticationPrincipal User user) {
        myPageService.updateProfile(request.getNickname(), request.getIntro(), request.getBadge(), user);
        return ResponseEntity.ok("마이페이지 수정 완료");
    }

    // 🔹 한줄 소개만 수정
    @PutMapping("/intro")
    public ResponseEntity<?> updateIntro(@RequestBody String intro,
                                         @AuthenticationPrincipal User user) {
        myPageService.updateIntro(intro, user);
        return ResponseEntity.ok("한줄소개 수정 완료");
    }

    // 🔹 프로필 이미지 업로드
    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile image,
                                                     @AuthenticationPrincipal User user) {
        String url = myPageService.updateProfileImage(user, image);
        return ResponseEntity.ok(url);
    }

    // 🔹 대표 뱃지 선택
    @PutMapping("/representative-badge")
    public ResponseEntity<?> updateRepresentativeBadge(@RequestBody RepresentativeBadgeRequest request,
                                                       @AuthenticationPrincipal User user) {
        myPageService.updateRepresentativeBadge(user, request.getBadgeName());
        return ResponseEntity.ok("대표 뱃지 변경 완료");
    }

    // 🔹 내가 가진 뱃지 전체 조회
    @GetMapping("/badges")
    public ResponseEntity<List<String>> getMyBadges(@AuthenticationPrincipal User user) {
        List<String> badges = myPageService.getUserBadges(user).stream()
                .map(Badge::getName)
                .toList();
        return ResponseEntity.ok(badges);
    }

    // 🔹 [테스트용] 거리 기반 뱃지 지급 강제 실행
    @PostMapping("/badges/check")
    public ResponseEntity<?> checkBadges(@AuthenticationPrincipal User user) {
        myPageService.checkAndGiveDistanceBadge(user);
        return ResponseEntity.ok("뱃지 체크 완료 ✅");
    }

    // 🔹 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("로그아웃 완료 (프론트에서 토큰 삭제 필요)");
    }
}