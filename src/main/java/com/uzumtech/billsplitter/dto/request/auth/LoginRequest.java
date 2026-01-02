package com.uzumtech.billsplitter.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "login required") String login,
                           @NotBlank(message = "password required") String password) {
}
