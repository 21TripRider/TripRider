package com.TripRider.TripRider.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    private String imageUrl;  // 이미지 경로

    private String location;  // 위치 텍스트

    private String hashtags;  // 해시태그 문자열

    private LocalDateTime createdAt;

    //좋아요 기능
    @Column(nullable = false)
    private int likeCount;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        // 하이버네이트가 기본 0을 넣지만, 방어적으로 가드
        if (this.likeCount < 0) this.likeCount = 0;
    }

    public void increaseLikeCount() { this.likeCount++; }
    public void decreaseLikeCount() { if (this.likeCount > 0) this.likeCount--; }
}
