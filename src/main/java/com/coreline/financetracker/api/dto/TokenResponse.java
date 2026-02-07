package com.coreline.financetracker.api.dto;

import java.time.Instant;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        Instant refreshExpiresAt
) {
}
