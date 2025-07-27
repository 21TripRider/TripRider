package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.dto.MyPageResponse;
import com.TripRider.TripRider.dto.MyPageUpdateRequest;
import com.TripRider.TripRider.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public String myPage(Model model) {
        MyPageResponse response = myPageService.getMyPage();
        model.addAttribute("user", response);
        return "mypage";
    }

    @PostMapping("/mypage")
    public String updateMyPage(@ModelAttribute MyPageUpdateRequest request) {
        myPageService.updateMyPage(request);
        return "redirect:/mypage";
    }
}
