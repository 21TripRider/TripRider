package com.TripRider.TripRider.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_account",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_provider_userid",
                columnNames = {"provider", "providerUserId"}
        ))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SocialAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Provider provider; // GOOGLE, KAKAO

    @Column(nullable = false, length = 128)
    private String providerUserId; // Google: id(sub), Kakao: id(숫자→문자열)

    @Column(length = 190)
    private String emailAtProvider; // 소셜이 내려준 이메일(없을 수 있음)

    private LocalDateTime connectedAt;
}
