package com.uzumtech.billsplitter.service.auth;

import com.uzumtech.billsplitter.dto.request.auth.LoginRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;

public interface WaiterAuthService {
    TokenResponse login(final LoginRequest loginRequest);
}
