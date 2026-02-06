package com.coreline.financetracker.enrichment.service;

import com.coreline.financetracker.common.time.ClockProvider;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.enrichment.model.*;
import com.coreline.financetracker.enrichment.rule.EnrichmentRule;
import com.coreline.financetracker.enrichment.store.EnrichmentStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EnrichmentService {

    private final List<EnrichmentRule> rules;
    private final EnrichmentStore store;
    private final ClockProvider clockProvider;

    public EnrichmentService(
            List<EnrichmentRule> rules,
            EnrichmentStore store,
            ClockProvider clockProvider
    ) {
        this.rules = rules;
        this.store = store;
        this.clockProvider = clockProvider;
    }

    public Optional<EnrichmentResult> enrich(Transaction transaction) {
        for (EnrichmentRule rule : rules) {
            Optional<Category> category = rule.apply(transaction);
            if (category.isPresent()) {
                EnrichmentResult result = new EnrichmentResult(
                        transaction.getId(),
                        category.get(),
                        EnrichmentSource.RULE,
                        rule.explanation(),
                        clockProvider.now()

                );
                store.save(result);
                return Optional.of(result);
            }
        }
        return Optional.empty();
    }
}
