package com.TripRider.TripRider.config;

import com.TripRider.TripRider.jwt.JwtAuthenticationFilter;
import com.TripRider.TripRider.service.mypage.UserDetailService; // ← 실제 경로 맞춰주세요
// import com.TripRider.TripRider.service.auth.CustomOAuth2UserService; // (서버 리다이렉트 OAuth 안 쓰면 불필요)

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailService userDetailService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // private final CustomOAuth2UserService customOAuth2UserService; // (서버 리다이렉트 방식이면 주석 해제)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS(Security 필터 체인에 올려야 실제 동작)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                // CSRF 비활성(모바일+JWT 환경)
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 미사용(JWT)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 기본 로그인/HTTP Basic 비활성
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // prod에서는 frameOptions DENY (H2 콘솔은 dev 전용 별도 설정)
                .headers(h -> h.frameOptions(f -> f.deny()))
                // 인증/인가 규칙
                .authorizeHttpRequests(auth -> auth
                        // Preflight 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 공개(permitAll) 엔드포인트
                        .requestMatchers("/api/auth/**").permitAll()                // 회원가입/로그인/소셜 토큰 교환
                        .requestMatchers("/api/users/check-nickname").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/jeju-weather/**").permitAll()
                        .requestMatchers("/api/jeju-weather/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()            // Actuator 쓸 때만

                        // 코스/조회 공개
                        .requestMatchers(HttpMethod.GET, "/api/custom/categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/custom/places").permitAll()
                        .requestMatchers("/api/custom/selection/**").permitAll()
                        .requestMatchers("/api/custom/courses/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/travel/riding/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/travel/nearby/**").permitAll()

                        // 업로드는 운영에선 인증 권장 (원하면 permitAll로 유지 가능)
                        .requestMatchers(HttpMethod.POST, "/api/upload").authenticated()
                        .requestMatchers("/uploads/**").permitAll() // 파일 서빙은 공개(필요시 조정)

                        // 좋아요는 인증 필요
                        .requestMatchers(HttpMethod.POST,   "/api/travel/riding/*/*/likes").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/travel/riding/*/*/likes").authenticated()

                        // 그 외는 인증
                        .anyRequest().authenticated()
                )
                // 리다이렉트 대신 JSON 에러로 응답(모바일 친화)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(401);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"UNAUTHORIZED\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(403);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"FORBIDDEN\"}");
                        })
                )

                // (선택) 서버 리다이렉트 OAuth를 쓸 때만 활성화하세요.
                // .oauth2Login(oauth2 -> oauth2
                //     .defaultSuccessUrl("/home", true)
                //     .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                // )

                // JWT 필터
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager (UserDetailsService + PasswordEncoder)
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

    // CORS 정책 (필요 시 오리진/메서드 제한하세요)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // 앱만 서비스하면 보통 *로 충분. 운영에서 특정 도메인만 허용하려면 아래처럼 교체:
        // cfg.setAllowedOrigins(List.of("https://api.yourdomain.com", "https://yourapp.page"));
        cfg.setAllowedOriginPatterns(List.of("*"));

        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With","Accept","Origin"));
        cfg.setAllowCredentials(true); // 쿠키 안 쓰면 false로도 가능
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
