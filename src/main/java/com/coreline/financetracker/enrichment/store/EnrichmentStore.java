package com.coreline.financetracker.enrichment.store;

import com.coreline.financetracker.enrichment.model.EnrichmentResult;
import com.coreline.financetracker.enrichment.model.TransactionEnrichment;
import com.coreline.financetracker.enrichment.repository.TransactionEnrichmentRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnrichmentStore {

    private final TransactionEnrichmentRepository repository;

    public EnrichmentStore(TransactionEnrichmentRepository repository) {
        this.repository = repository;
    }

    public void save(EnrichmentResult result, UUID userId) {
        TransactionEnrichment entity = new TransactionEnrichment(
                result.transactionId(),
                userId,
                result.category(),
                result.source(),
                result.explanation(),
                result.enrichedAt()
        );
        repository.save(entity);
    }

    public Optional<EnrichmentResult> findByTransactionId(UUID transactionId) {
        return repository.findById(transactionId)
                .map(this::toResult);
    }

    public Collection<EnrichmentResult> allByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResult)
                .toList();
    }

    private EnrichmentResult toResult(TransactionEnrichment entity) {
        return new EnrichmentResult(
                entity.getTransactionId(),
                entity.getCategory(),
                entity.getSource(),
                entity.getExplanation(),
                entity.getEnrichedAt()
        );
    }
}
