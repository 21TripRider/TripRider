package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.dto.MyPageUpdateRequest;
import com.TripRider.TripRider.service.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    // 🔹 마이페이지 GET - 페이지 렌더링
    @GetMapping
    public String showMyPage(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loginUser);
        return "mypage";
    }

    // 🔹 프로필 이미지 업로드
    @PostMapping("/profile-image")
    public String updateProfileImage(@RequestParam("image") MultipartFile image,
                                     HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        myPageService.updateProfileImage(loginUser, image);
        return "redirect:/mypage";
    }

    // 🔹 닉네임/뱃지 수정
    @PostMapping
    public String updateMyPage(@ModelAttribute MyPageUpdateRequest request,
                               HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        myPageService.updateProfile(
                request.getNickname(),
                request.getIntro(),
                request.getBadge(),
                loginUser
        );
        return "redirect:/mypage";
    }

    // 🔹 한줄 소개 수정 페이지
    @GetMapping("/intro-edit")
    public String editIntroPage(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loginUser);
        return "mypage_intro";
    }

    // 🔹 한줄 소개 저장
    @PostMapping("/intro-edit")
    public String updateIntro(@RequestParam("intro") String intro, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        myPageService.updateIntro(intro, loginUser);
        return "redirect:/mypage";
    }
}
