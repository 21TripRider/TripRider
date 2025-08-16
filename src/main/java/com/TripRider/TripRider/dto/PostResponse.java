package com.TripRider.TripRider.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private String location;
    private String hashtags;
    private String writer;
    private int likeCount;
    private boolean liked;
    private int commentCount;
}
