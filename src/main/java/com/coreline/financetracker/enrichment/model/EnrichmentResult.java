package com.coreline.financetracker.enrichment.model;

import java.time.Instant;
import java.util.UUID;

public record EnrichmentResult(
        UUID transactionId,
        Category category,
        EnrichmentSource source,
        String explanation,
        Instant enrichedAt
) {
}