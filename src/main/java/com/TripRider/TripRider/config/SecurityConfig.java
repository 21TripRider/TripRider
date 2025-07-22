package com.TripRider.TripRider.config;

import com.TripRider.TripRider.service.CustomOAuth2UserService;
import com.TripRider.TripRider.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailService userService; //일반 로그인용
    private final CustomOAuth2UserService customOAuth2UserService; //소셜 로그인용

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/user").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form //일반 로그인
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                )
                .oauth2Login(oauth2 -> oauth2 //소셜 로그인
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 매핑 서비스 연결
                        )
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                )
                .csrf(csrf -> csrf.disable()) // 실습을 위한 csrf 비활성화
                .build();
    }


    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
            throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService) // 사용자 정보 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
