package com.uzumtech.billsplitter.service.auth.token;

import com.uzumtech.billsplitter.entity.RefreshTokenEntity;
import com.uzumtech.billsplitter.entity.user.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(final CustomUserDetails userDetails);

    RefreshTokenEntity findByToken(final String token);

    UserDetails getUserDetails(final String token);

    void verifyExpiration(final RefreshTokenEntity token);
}
