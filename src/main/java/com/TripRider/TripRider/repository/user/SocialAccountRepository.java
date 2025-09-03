package com.TripRider.TripRider.repository.user;

import com.TripRider.TripRider.domain.user.Provider;
import com.TripRider.TripRider.domain.user.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findByProviderAndProviderUserId(Provider provider, String providerUserId);
}
