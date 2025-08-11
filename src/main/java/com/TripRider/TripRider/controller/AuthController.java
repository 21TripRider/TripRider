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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private static final String DEFAULT_NICK_FOR_SOCIAL = "사용자";
    private static final String DEFAULT_NICK_FOR_LOCAL  = "익명";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /** 회원가입(일반) **/
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AddUserRequest request) {
        boolean result = userService.save(request);
        if (!result) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "이미 존재하는 이메일입니다."));
        }
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    /** 로그인(일반) **/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AddUserRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtTokenProvider.createToken(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        boolean needNickname = needNickname(user.getNickname());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "isNewUser", false,
                "needNickname", needNickname
        ));
    }

    /** 카카오 로그인 **/
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

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
        Map<String, Object> profile = kakaoAccount != null
                ? (Map<String, Object>) kakaoAccount.get("profile") : null;

        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        if (email == null || email.isBlank()) {
            email = "kakao_" + UUID.randomUUID() + "@triprider.com";
        }
        String nicknameFromProvider = (profile != null) ? (String) profile.get("nickname") : null;

        boolean isNewUser = false;
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            isNewUser = true;
            user = User.builder()
                    .email(email)
                    .password("") // 소셜은 비번 X
                    .nickname(nicknameOrDefault(nicknameFromProvider, DEFAULT_NICK_FOR_SOCIAL))
                    .build();
            userRepository.save(user);
        }

        String jwt = jwtTokenProvider.createToken(email);
        boolean needNickname = needNickname(user.getNickname());

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "isNewUser", isNewUser,
                "needNickname", needNickname
        ));
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
        String email = googleUser != null ? (String) googleUser.get("email") : null;
        String name  = googleUser != null ? (String) googleUser.get("name") : null;

        if (email == null || email.isBlank()) {
            email = "google_" + UUID.randomUUID() + "@triprider.com";
        }

        boolean isNewUser = false;
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            isNewUser = true;
            user = User.builder()
                    .email(email)
                    .password("")
                    .nickname(nicknameOrDefault(name, DEFAULT_NICK_FOR_SOCIAL))
                    .build();
            userRepository.save(user);
        }

        String jwt = jwtTokenProvider.createToken(email);
        boolean needNickname = needNickname(user.getNickname());

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "isNewUser", isNewUser,
                "needNickname", needNickname
        ));
    }

    private boolean needNickname(String nickname) {
        if (nickname == null) return true;
        String n = nickname.trim();
        if (n.isEmpty()) return true;
        // 기본 닉네임은 최초 설정 요구
        if (DEFAULT_NICK_FOR_SOCIAL.equals(n) || DEFAULT_NICK_FOR_LOCAL.equals(n)) return true;
        return false;
    }

    private String nicknameOrDefault(String fromProvider, String def) {
        if (fromProvider == null) return def;
        String n = fromProvider.trim();
        return n.isEmpty() ? def : n;
    }
}
