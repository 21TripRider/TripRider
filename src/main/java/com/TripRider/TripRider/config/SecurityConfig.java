package com.TripRider.TripRider.config;

import com.TripRider.TripRider.jwt.JwtAuthenticationFilter;
import com.TripRider.TripRider.service.CustomOAuth2UserService;
import com.TripRider.TripRider.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailService userDetailService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //  CSRF 해제 (개발용)
                .csrf(csrf -> csrf.disable())

                //  H2 콘솔 iframe 허용
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                //  기본 로그인/HTTP Basic 비활성화 (JWT 방식 사용)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                //  세션을 사용하지 않음 (JWT 방식)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //  요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",           // 회원가입, 로그인, 소셜 로그인
                                "/api/users/check-nickname", // 닉네임 중복체크
                                "/h2-console/**",         //  H2 콘솔 허용
                                "/home",                   // 홈 페이지
                                "/api/jeju-weather",
                                "/jeju-weather",
                                "/uploads/**", "/api/upload",
                                "/images/**"
                        ).permitAll()

                        // 맞춤형 코스 탭: 테스트용으로 공개
                        .requestMatchers(HttpMethod.GET,  "/api/custom/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/custom/places").permitAll()
                        .requestMatchers("/api/custom/selection/**").permitAll()
                        .requestMatchers("/api/custom/courses/**").permitAll()

                        // 라이딩 코스: 조회는 전부 공개
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/travel/riding/**").permitAll()

                        // nearby(주변 장소) 조회 공개
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/travel/nearby/**").permitAll()

                        // 좋아요(등록/취소)만 인증 필요
                        // {category}/{id} 형태를 쓰지 말고, 아래처럼 와일드카드 사용!
                        .requestMatchers(org.springframework.http.HttpMethod.POST,   "/api/travel/riding/*/*/likes").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/travel/riding/*/*/likes").authenticated()
                        .anyRequest().authenticated()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/home", true)
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                )

                // JWT 필터 등록
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}