package com.coreline.financetracker.enrichment.store;

import com.coreline.financetracker.enrichment.model.EnrichmentResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnrichmentStore {

    private final Map<UUID, EnrichmentResult> enrichments = new HashMap<>();

    public void save(EnrichmentResult result) {
        enrichments.put(result.transactionId(), result);
    }

    public Optional<EnrichmentResult> findByTransactionId(UUID transactionId) {
        return Optional.ofNullable(enrichments.get(transactionId));
    }

    public Collection<EnrichmentResult> all() {
        return Collections.unmodifiableCollection(enrichments.values());
    }
}
