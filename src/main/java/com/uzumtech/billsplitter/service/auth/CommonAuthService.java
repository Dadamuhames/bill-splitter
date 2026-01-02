package com.uzumtech.billsplitter.service.auth;

import com.uzumtech.billsplitter.dto.request.auth.RefreshRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;

public interface CommonAuthService {
    TokenResponse refresh(final RefreshRequest request);
}
