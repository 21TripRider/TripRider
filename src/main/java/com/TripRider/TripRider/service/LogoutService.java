package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.BlacklistedToken;
import com.TripRider.TripRider.jwt.JwtTokenProvider;
import com.TripRider.TripRider.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void blacklistToken(String token) {
        LocalDateTime expiry = jwtTokenProvider.getExpiry(token);

        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .token(token)
                .expiredAt(expiry)
                .build();

        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }
}
