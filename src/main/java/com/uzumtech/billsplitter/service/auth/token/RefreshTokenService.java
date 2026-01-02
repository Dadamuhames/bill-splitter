package com.uzumtech.billsplitter.service.auth.token;

import com.uzumtech.billsplitter.entity.RefreshTokenEntity;
import com.uzumtech.billsplitter.entity.user.CustomUserDetails;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(final CustomUserDetails userDetails);

    RefreshTokenEntity findByToken(final String token);

    CustomUserDetails getUserDetails(final RefreshTokenEntity refreshToken);

    void verifyExpiration(final RefreshTokenEntity token);

    void expireToken(final RefreshTokenEntity token);
}
