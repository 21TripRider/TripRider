package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.NicknameUpdateRequest;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // 닉네임 사용 가능 여부 체크 (공개)
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        String validated = normalize(nickname);
        if (!isValidNickname(validated)) {
            return ResponseEntity.badRequest().body(Map.of("available", false, "reason", "형식오류"));
        }
        boolean exists = userRepository.existsByNickname(validated);
        return ResponseEntity.ok(Map.of("available", !exists));
    }

    // 내 닉네임 저장/수정 (JWT 필요)
    @PatchMapping("/me/nickname")
    public ResponseEntity<?> updateMyNickname(
            @RequestBody NicknameUpdateRequest req,
            @AuthenticationPrincipal User me
    ) {
        String target = normalize(req.getNickname());
        if (!isValidNickname(target)) {
            return ResponseEntity.badRequest().body(Map.of("error", "닉네임 형식이 올바르지 않습니다."));
        }
        if (userRepository.existsByNickname(target)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "이미 사용중인 닉네임"));
        }
        me.setNickname(target);
        userRepository.save(me);
        return ResponseEntity.ok(Map.of("message", "OK", "nickname", target));
    }

    // ===== 유틸 =====
    private String normalize(String raw) {
        return raw == null ? "" : raw.trim();
    }

    // 2~16자, 한/영/숫자/밑줄 허용
    private boolean isValidNickname(String s) {
        if (s == null) return false;
        return s.matches("^[A-Za-z0-9가-힣_]{2,16}$");
    }
}