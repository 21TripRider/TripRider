package com.TripRider.TripRider.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class MyPageResponse {
    private String nickname;
    private String email;
    private String profileImage;
    private String intro;
    private int totalDistance;

    private String badge;
}
