package com.coreline.financetracker.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmRequest(
        @NotBlank String email,
        @NotBlank String resetToken,
        @NotBlank @Size(min = 6, max = 100) String newPassword
) {
}
