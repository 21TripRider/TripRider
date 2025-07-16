package com.TripRider.TripRider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    //일단 이메일, 비번만 해놨음 나중에 배지같은거 추가
    private String email;
    private String password;
}