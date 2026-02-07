package com.coreline.financetracker.api.dto;

public record PasswordResetResponse(
        String message,
        String resetToken
) {
}
