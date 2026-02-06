package com.coreline.financetracker.api.dto;

import java.time.Instant;

public record ExportRunDto(
        boolean success,
        String message,
        Instant finishedAt
) {
}
