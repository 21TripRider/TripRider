package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.AddUserRequest;
import com.TripRider.TripRider.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    // 회원가입 메소드
    @PostMapping("/user")
    public String signup(AddUserRequest request, Model model) {
        boolean result = userService.save(request); // true = 성공, false = 중복

        if (!result) {
            model.addAttribute("error", "이미 존재하는 이메일입니다.");
            return "signup";
        }

        return "redirect:/login";
    }

    // 로그아웃 메소드
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
