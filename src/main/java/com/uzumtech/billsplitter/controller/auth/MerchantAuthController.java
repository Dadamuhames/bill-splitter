package com.uzumtech.billsplitter.controller.auth;

import com.uzumtech.billsplitter.dto.request.auth.LoginRequest;
import com.uzumtech.billsplitter.dto.request.auth.MerchantRegistrationRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.service.auth.MerchantAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchant/auth")
public class MerchantAuthController {
    private final MerchantAuthService merchantAuthService;

    @PostMapping("/registration")
    public ResponseEntity<TokenResponse> registration(@Valid @RequestBody final MerchantRegistrationRequest request) {
        TokenResponse tokenResponse = merchantAuthService.register(request);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody final LoginRequest request) {
        TokenResponse tokenResponse = merchantAuthService.login(request);

        return ResponseEntity.ok(tokenResponse);
    }
}
