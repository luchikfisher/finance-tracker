package com.coreline.financetracker.api.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(
        @NotBlank String username
) {
}
