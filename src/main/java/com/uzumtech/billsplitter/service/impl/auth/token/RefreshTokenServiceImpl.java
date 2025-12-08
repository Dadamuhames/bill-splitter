package com.uzumtech.billsplitter.service.impl.auth.token;

import com.uzumtech.billsplitter.config.property.JwtProperty;
import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.entity.RefreshTokenEntity;
import com.uzumtech.billsplitter.entity.user.CustomUserDetails;
import com.uzumtech.billsplitter.exception.RefreshTokenException;
import com.uzumtech.billsplitter.repository.RefreshTokenRepository;
import com.uzumtech.billsplitter.service.auth.token.RefreshTokenService;
import com.uzumtech.billsplitter.service.auth.userdetails.UserDetailsServiceDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperty jwtProperty;
    private final UserDetailsServiceDispatcher detailsServiceDispatcher;


    @Override
    public RefreshTokenEntity createRefreshToken(final CustomUserDetails userDetails) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
            .token(UUID.randomUUID().toString())
            .userRole(userDetails.getUserRole())
            .subject(userDetails.getUsername())
            .expiryDate(Instant.now().plusMillis(jwtProperty.getRefreshTtl()))
            .build();

        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public RefreshTokenEntity findByToken(final String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException(Error.REFRESH_TOKEN_INVALID_CODE));
    }


    @Override
    public UserDetails getUserDetails(final String token) {
        RefreshTokenEntity refreshToken = findByToken(token);

        return detailsServiceDispatcher.loadUserByLoginAndRole(refreshToken.getSubject(), refreshToken.getUserRole());
    }


    @Override
    public void verifyExpiration(final RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(Error.REFRESH_TOKEN_INVALID_CODE);
        }
    }

    public void deleteToken(final RefreshTokenEntity token) {
        refreshTokenRepository.delete(token);
    }

    public void deleteByToken(final String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
