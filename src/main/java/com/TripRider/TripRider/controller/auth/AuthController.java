package com.TripRider.TripRider.controller.auth;

import com.TripRider.TripRider.domain.user.Provider;
import com.TripRider.TripRider.domain.user.SocialAccount;
import com.TripRider.TripRider.domain.user.User;
import com.TripRider.TripRider.dto.auth.AddUserRequest;
import com.TripRider.TripRider.jwt.JwtTokenProvider;
import com.TripRider.TripRider.repository.user.SocialAccountRepository;
import com.TripRider.TripRider.repository.user.UserRepository;
import com.TripRider.TripRider.service.mypage.LogoutService;
import com.TripRider.TripRider.service.mypage.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private static final String DEFAULT_NICK_FOR_SOCIAL = "사용자";
    private static final String DEFAULT_NICK_FOR_LOCAL  = "익명";

    private final SocialAccountRepository socialAccountRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final LogoutService logoutService;

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

    /** 이메일 중복 확인 **/
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        String normalized = email == null ? "" : email.trim().toLowerCase();
        boolean exists = userRepository.existsByEmail(normalized);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    /** 로그인(일반) **/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AddUserRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtTokenProvider.createToken(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        boolean needNick = requiresNickname(user.getNickname());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "isNewUser", false,
                "needNickname", needNick
        ));
    }

    /** 로그아웃 **/
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            logoutService.blacklistToken(token);
        }
        return ResponseEntity.ok(Map.of("message", "로그아웃 완료 ✅"));
    }

    /** 카카오 로그인 — (provider, providerUserId) 우선 매칭 / 동일 이메일 자동합체 금지 **/
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        Map kakao = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        ).getBody();

        String providerUserId = String.valueOf(kakao.get("id"));
        Map<String,Object> kakaoAccount = kakao.get("kakao_account") instanceof Map
                ? (Map<String,Object>) kakao.get("kakao_account") : null;
        String emailAtProvider = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;

        // 1) 링크 매칭
        var linked = socialAccountRepository.findByProviderAndProviderUserId(Provider.KAKAO, providerUserId);
        if (linked.isPresent()) {
            User user = linked.get().getUser();
            String jwt = jwtTokenProvider.createToken(user.getEmail());
            boolean needNick = requiresNickname(user.getNickname());
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "email", user.getEmail(),
                    "nickname", user.getNickname(),
                    "isNewUser", false,
                    "needNickname", needNick
            ));
        }

        // 2) 같은 이메일의 기존 User가 있으면 자동합체 금지 → 409
        if (emailAtProvider != null && userRepository.findByEmail(emailAtProvider).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "code", "LINK_REQUIRED",
                    "message", "동일 이메일의 기존 계정이 있습니다. 계정 연동이 필요합니다.",
                    "provider", "KAKAO",
                    "providerUserId", providerUserId,
                    "emailAtProvider", emailAtProvider
            ));
        }

        // 3) 신규 User + SocialAccount
        String email = (emailAtProvider == null || emailAtProvider.isBlank())
                ? ("kakao_" + UUID.randomUUID() + "@triprider.com")
                : emailAtProvider;

        String nickname = generateDefaultNickname();
        User user = userRepository.save(User.builder()
                .email(email)
                .password("") // 소셜은 비번 없음
                .nickname(nickname)
                .build());

        socialAccountRepository.save(SocialAccount.builder()
                .user(user)
                .provider(Provider.KAKAO)
                .providerUserId(providerUserId)
                .emailAtProvider(emailAtProvider)
                .connectedAt(LocalDateTime.now())
                .build());

        String jwt = jwtTokenProvider.createToken(user.getEmail());
        boolean needNick = requiresNickname(user.getNickname());
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "isNewUser", true,
                "needNickname", needNick
        ));
    }

    /** 구글 로그인 — (provider, providerUserId) 우선 매칭 / 동일 이메일 자동합체 금지 **/
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        Map google = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        ).getBody();

        String providerUserId = google.get("id") != null ? String.valueOf(google.get("id")) : null; // (=sub)
        String emailAtProvider = google.get("email") != null ? (String) google.get("email") : null;

        if (providerUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "google profile missing id"));
        }

        // 1) 링크 매칭
        var linked = socialAccountRepository.findByProviderAndProviderUserId(Provider.GOOGLE, providerUserId);
        if (linked.isPresent()) {
            User user = linked.get().getUser();
            String jwt = jwtTokenProvider.createToken(user.getEmail());
            boolean needNick = requiresNickname(user.getNickname());
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "email", user.getEmail(),
                    "nickname", user.getNickname(),
                    "isNewUser", false,
                    "needNickname", needNick
            ));
        }

        // 2) 같은 이메일의 기존 User가 있으면 자동합체 금지 → 409
        if (emailAtProvider != null && userRepository.findByEmail(emailAtProvider).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "code", "LINK_REQUIRED",
                    "message", "동일 이메일의 기존 계정이 있습니다. 계정 연동이 필요합니다.",
                    "provider", "GOOGLE",
                    "providerUserId", providerUserId,
                    "emailAtProvider", emailAtProvider
            ));
        }

        // 3) 신규 User + SocialAccount
        String email = (emailAtProvider == null || emailAtProvider.isBlank())
                ? ("google_" + UUID.randomUUID() + "@triprider.com")
                : emailAtProvider;

        String nickname = generateDefaultNickname();
        User user = userRepository.save(User.builder()
                .email(email)
                .password("") // 소셜은 비번 없음
                .nickname(nickname)
                .build());

        socialAccountRepository.save(SocialAccount.builder()
                .user(user)
                .provider(Provider.GOOGLE)
                .providerUserId(providerUserId)
                .emailAtProvider(emailAtProvider)
                .connectedAt(LocalDateTime.now())
                .build());

        String jwt = jwtTokenProvider.createToken(user.getEmail());
        boolean needNick = requiresNickname(user.getNickname());
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "isNewUser", true,
                "needNickname", needNick
        ));
    }

    /** 계정 연동(409 받은 후, 기존 로그인 상태에서 호출) **/
    @PostMapping("/link")
    public ResponseEntity<?> linkAccount(@RequestBody Map<String, String> body,
                                         @org.springframework.security.core.annotation.AuthenticationPrincipal User me) {
        String providerStr = body.get("provider");          // "GOOGLE" | "KAKAO"
        String providerUserId = body.get("providerUserId"); // 409에서 받은 값
        String emailAtProvider = body.get("emailAtProvider");

        Provider provider = Provider.valueOf(providerStr);

        if (socialAccountRepository.findByProviderAndProviderUserId(provider, providerUserId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","이미 다른 계정과 연동됨"));
        }

        socialAccountRepository.save(SocialAccount.builder()
                .user(me)
                .provider(provider)
                .providerUserId(providerUserId)
                .emailAtProvider(emailAtProvider)
                .connectedAt(LocalDateTime.now())
                .build());

        String jwt = jwtTokenProvider.createToken(me.getEmail());
        return ResponseEntity.ok(Map.of("message","linked","token", jwt));
    }

    // ===== Helper Methods =====

    /** 닉네임이 비어있거나 기본값이면 true */
    private boolean requiresNickname(String nickname) {
        if (nickname == null) return true;
        String n = nickname.trim();
        if (n.isEmpty()) return true;
        return DEFAULT_NICK_FOR_SOCIAL.equals(n) || DEFAULT_NICK_FOR_LOCAL.equals(n);
    }

    /** "사용자"가 이미 있으면 "사용자1234" 식으로 충돌 회피 */
    private String generateDefaultNickname() {
        String nickname = DEFAULT_NICK_FOR_SOCIAL;
        int tries = 0;
        while (userRepository.existsByNickname(nickname) && tries++ < 20) {
            nickname = DEFAULT_NICK_FOR_SOCIAL + String.format("%04d", (int)(Math.random()*10000));
        }
        if (tries > 20) {
            nickname = DEFAULT_NICK_FOR_SOCIAL + "_" + UUID.randomUUID().toString().substring(0, 8);
        }
        return nickname;
    }
}
