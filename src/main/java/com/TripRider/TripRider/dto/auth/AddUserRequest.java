package com.TripRider.TripRider.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;
    private String nickname; // 🔹 닉네임 추가
}