package com.uzumtech.billsplitter.service.impl;

import com.uzumtech.billsplitter.dto.request.auth.RefreshRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.entity.RefreshTokenEntity;
import com.uzumtech.billsplitter.entity.user.CustomUserDetails;
import com.uzumtech.billsplitter.service.auth.CommonAuthService;
import com.uzumtech.billsplitter.service.auth.token.RefreshTokenService;
import com.uzumtech.billsplitter.service.auth.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonAuthServiceImpl implements CommonAuthService {
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public TokenResponse refresh(final RefreshRequest request) {
        RefreshTokenEntity refreshToken = refreshTokenService.findByToken(request.token());

        refreshTokenService.verifyExpiration(refreshToken);

        CustomUserDetails user = refreshTokenService.getUserDetails(refreshToken);

        refreshTokenService.expireToken(refreshToken);

        return tokenService.createPair(user);
    }
}
