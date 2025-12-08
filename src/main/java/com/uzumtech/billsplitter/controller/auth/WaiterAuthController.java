package com.uzumtech.billsplitter.controller.auth;


import com.uzumtech.billsplitter.dto.request.auth.LoginRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.service.auth.WaiterAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiter/auth")
public class WaiterAuthController {
    private final WaiterAuthService waiterAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody final LoginRequest request) {
        TokenResponse tokenResponse = waiterAuthService.login(request);

        return ResponseEntity.ok(tokenResponse);
    }
}
