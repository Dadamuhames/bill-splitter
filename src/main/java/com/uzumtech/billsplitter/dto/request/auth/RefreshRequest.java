package com.uzumtech.billsplitter.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank(message = "token required") String token) {
}
