package com.TripRider.TripRider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;
    private String nickname; // 🔹 닉네임 추가
}
