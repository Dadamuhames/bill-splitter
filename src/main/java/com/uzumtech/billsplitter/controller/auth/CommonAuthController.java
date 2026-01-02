package com.uzumtech.billsplitter.controller.auth;


import com.uzumtech.billsplitter.dto.request.auth.RefreshRequest;
import com.uzumtech.billsplitter.dto.response.TokenResponse;
import com.uzumtech.billsplitter.service.auth.CommonAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/auth")
public class CommonAuthController {
    private final CommonAuthService commonAuthService;


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody final RefreshRequest request) {
        TokenResponse response = commonAuthService.refresh(request);

        return ResponseEntity.ok(response);
    }
}
