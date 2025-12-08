package com.uzumtech.billsplitter.dto.request.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MerchantRegistrationRequest(@NotBlank(message = "companyName required") String name,
                                          @NotBlank(message = "login required") String login,
                                          @NotBlank(message = "taxNumber should consists of 9 digits") @Size(min = 9, max = 9, message = "taxNumber should consists of 9 digits") String taxNumber,
                                          @NotBlank(message = "password required") String password,
                                          @NotBlank(message = "password confirmation required") String passwordConfirm) {


    @AssertTrue(message = "Passwords should match")
    private boolean isPasswordConfirmed() {
        return this.password.equals(this.passwordConfirm);
    }
}
