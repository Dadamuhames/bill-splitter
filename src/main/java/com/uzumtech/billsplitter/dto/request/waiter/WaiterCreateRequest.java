package com.uzumtech.billsplitter.dto.request.waiter;

import jakarta.validation.constraints.NotBlank;

public record WaiterCreateRequest(@NotBlank(message = "fullName required") String fullName,
                                  @NotBlank(message = "login required") String login,
                                  @NotBlank(message = "password required") String password) {}
