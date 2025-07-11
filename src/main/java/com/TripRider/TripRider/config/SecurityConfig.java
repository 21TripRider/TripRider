package com.TripRider.TripRider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//H2 데이터베이스 사용시 spring security 잠시 해제
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // CSRF 예외
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()) // H2 콘솔 iframe 허용
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()  // ✅ h2-console 허용
                        .anyRequest().authenticated()  // 나머지는 인증 필요
                )
                .formLogin(Customizer.withDefaults());  // 로그인 폼 사용

        return http.build();
    }
}

