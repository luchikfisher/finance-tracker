package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.enrichment.model.EnrichmentResult;

import java.time.Instant;
import java.util.UUID;

public record EnrichmentDto(
        UUID transactionId,
        String category,
        String source,
        String explanation,
        Instant enrichedAt
) {
    public static EnrichmentDto from(EnrichmentResult result) {
        return new EnrichmentDto(
                result.transactionId(),
                result.category().name(),
                result.source().name(),
                result.explanation(),
                result.enrichedAt()
        );
    }
}
