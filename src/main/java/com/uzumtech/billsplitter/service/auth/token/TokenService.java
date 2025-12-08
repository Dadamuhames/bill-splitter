package com.uzumtech.billsplitter.service.auth.token;

import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.entity.user.CustomUserDetails;

public interface TokenService {
    TokenResponse createPair(final CustomUserDetails user);
}
