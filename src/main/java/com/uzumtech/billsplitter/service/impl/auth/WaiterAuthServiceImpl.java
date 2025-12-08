package com.uzumtech.billsplitter.service.impl.auth;


import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.dto.request.auth.LoginRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.exception.PasswordInvalidException;
import com.uzumtech.billsplitter.service.auth.token.TokenService;
import com.uzumtech.billsplitter.service.auth.WaiterAuthService;
import com.uzumtech.billsplitter.service.impl.auth.userdetails.WaiterDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaiterAuthServiceImpl implements WaiterAuthService {
    private final WaiterDetailService waiterDetailService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        WaiterEntity waiter = (WaiterEntity) waiterDetailService.loadUserByUsername(loginRequest.login());

        if (!passwordEncoder.matches(loginRequest.password(), waiter.getPassword())) {
            throw new PasswordInvalidException(Error.PASSWORD_INVALID_CODE);
        }

        return tokenService.createPair(waiter);
    }
}
