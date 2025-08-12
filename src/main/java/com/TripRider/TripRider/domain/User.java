package com.TripRider.TripRider.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(
        name = "app_user",
        indexes = {
                @Index(name = "idx_app_user_email", columnList = "email", unique = true),
                @Index(name = "idx_app_user_nickname", columnList = "nickname", unique = true)
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 190, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 32, unique = true) // 닉네임 유니크 + 길이 제한
    private String nickname;

    private String badge;
    private String intro;
    private String profileImage;
    private int totalDistance;

    public void updateProfile(String nickname, String intro, String badge) {
        this.nickname = nickname;
        this.intro = intro;
        this.badge = badge;
    }

    public void updateProfileImage(String imagePath) {
        this.profileImage = imagePath;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}