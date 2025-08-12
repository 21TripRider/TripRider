package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId(); // google or kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String nickname = null;

        if ("google".equals(registrationId)) {
            // ✅ 구글 응답 처리
            email = (String) attributes.get("email");
            nickname = (String) attributes.get("name");

        } else if ("kakao".equals(registrationId)) {
            // ✅ 카카오 응답 처리
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            nickname = (String) profile.get("nickname");

            // ✅ 카카오 이메일이 없을 경우 대체값 생성
            if (email == null || email.isEmpty()) {
                email = "kakao_" + UUID.randomUUID() + "@triprider.com";
            }
        }

        // ✅ 방어 코드
        if (email == null) {
            throw new RuntimeException("이메일을 가져올 수 없습니다.");
        }

        final String finalEmail = email;
        final String finalNickname = (nickname != null && !nickname.isEmpty()) ? nickname : "사용자";

        // ✅ 기존 사용자 조회 또는 새로 생성
        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(finalEmail)
                                .nickname(finalNickname)
                                .password("") // 소셜 로그인은 비밀번호 없음
                                .build()
                ));

        // ✅ 🔥 무조건 세션 저장 (새 사용자든 기존 사용자든)
        HttpServletRequest servletRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = servletRequest.getSession();
        session.setAttribute("loginUser", user);

        // ✅ Spring Security 인증용 OAuth2User 객체 반환
        Map<String, Object> modifiedAttributes = new HashMap<>();
        modifiedAttributes.put("email", finalEmail);
        modifiedAttributes.put("nickname", finalNickname);

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("user")),
                modifiedAttributes,
                "email"
        );
    }
}