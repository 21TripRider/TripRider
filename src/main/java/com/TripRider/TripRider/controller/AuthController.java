package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.AddUserRequest;
import com.TripRider.TripRider.jwt.JwtTokenProvider;
import com.TripRider.TripRider.repository.UserRepository;
import com.TripRider.TripRider.service.UserService;
import com.TripRider.TripRider.domain.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /** 회원가입 **/
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AddUserRequest request) {
        boolean result = userService.save(request);
        if (!result) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "이미 존재하는 이메일입니다."));
        }
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    /** 로그인 **/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AddUserRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtTokenProvider.createToken(request.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    /** 카카오 로그인 **/
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken"); // Flutter에서 보낸 카카오 액세스 토큰

        // 1. 카카오 서버에서 사용자 정보 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> kakaoUser = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUser.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // email이 없으면 임시 생성
        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            email = "kakao_" + UUID.randomUUID() + "@triprider.com";
        }
        String nickname = (String) profile.get("nickname");

        // final 변수로 람다 문제 해결
        String finalEmail = email;

        // 2. DB에 사용자 저장 또는 기존 유저 찾기
        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(finalEmail)
                                .nickname(nickname)
                                .password("") // 소셜 로그인은 비번 X
                                .build()
                ));

        // 3. JWT 발급
        String jwt = jwtTokenProvider.createToken(finalEmail);

        // 4. JWT를 Flutter로 반환
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    /** 구글 로그인 **/
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> googleUser = response.getBody();
        String email = (String) googleUser.get("email");
        String name = (String) googleUser.get("name");

        if (email == null) {
            email = "google_" + UUID.randomUUID() + "@triprider.com";
        }

        String finalEmail = email;
        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(finalEmail)
                                .nickname(name)
                                .password("")
                                .build()
                ));

        String jwt = jwtTokenProvider.createToken(finalEmail);
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
