package com.coreline.financetracker.api.dto;

import java.time.Instant;

public record HealthDto(
        String status,
        Instant time
) {
}
