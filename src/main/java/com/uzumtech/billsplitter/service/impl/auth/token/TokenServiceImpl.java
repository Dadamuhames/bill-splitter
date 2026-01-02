package com.uzumtech.billsplitter.service.impl.auth.token;


import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.entity.RefreshTokenEntity;
import com.uzumtech.billsplitter.entity.user.CustomUserDetails;
import com.uzumtech.billsplitter.service.auth.token.JwtService;
import com.uzumtech.billsplitter.service.auth.token.RefreshTokenService;
import com.uzumtech.billsplitter.service.auth.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenResponse createPair(final CustomUserDetails user) {
        String accessToken = jwtService.generateToken(user);

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken.getToken());
    }
}
