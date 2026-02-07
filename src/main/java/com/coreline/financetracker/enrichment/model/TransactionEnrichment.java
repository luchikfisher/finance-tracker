package com.coreline.financetracker.enrichment.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transaction_enrichments")
public class TransactionEnrichment {

    @Id
    private UUID transactionId;

    @Column
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrichmentSource source;

    @Column(nullable = false)
    private String explanation;

    @Column(nullable = false)
    private Instant enrichedAt;

    protected TransactionEnrichment() {
        // JPA
    }

    public TransactionEnrichment(
            UUID transactionId,
            UUID userId,
            Category category,
            EnrichmentSource source,
            String explanation,
            Instant enrichedAt
    ) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.category = category;
        this.source = source;
        this.explanation = explanation;
        this.enrichedAt = enrichedAt;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Category getCategory() {
        return category;
    }

    public EnrichmentSource getSource() {
        return source;
    }

    public String getExplanation() {
        return explanation;
    }

    public Instant getEnrichedAt() {
        return enrichedAt;
    }
}
