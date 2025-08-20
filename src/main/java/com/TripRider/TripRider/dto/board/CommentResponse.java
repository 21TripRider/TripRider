package com.TripRider.TripRider.dto.board;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String user;          // 작성자 닉네임
    private String content;
    private LocalDateTime createdAt;
    private boolean mine;         // 내가 쓴 댓글인지
}