package com.uzumtech.billsplitter.service.auth;

import com.uzumtech.billsplitter.dto.request.auth.LoginRequest;
import com.uzumtech.billsplitter.dto.request.auth.MerchantRegistrationRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;

public interface MerchantAuthService {
    TokenResponse register(final MerchantRegistrationRequest request);

    TokenResponse login(final LoginRequest request);
}
