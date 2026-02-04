package com.coreline.financetracker.orchestration.model;

import java.time.Instant;

public record PipelineResult(
        PipelineStatus status,
        Instant finishedAt,
        String message
) {
}
