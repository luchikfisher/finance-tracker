package com.coreline.financetracker.analytics.service;

import com.coreline.financetracker.analytics.model.CategoryBreakdown;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.model.TransactionDirection;
import com.coreline.financetracker.enrichment.model.Category;
import com.coreline.financetracker.enrichment.store.EnrichmentStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CategoryAnalyticsService {

    private final EnrichmentStore enrichmentStore;

    public CategoryAnalyticsService(EnrichmentStore enrichmentStore) {
        this.enrichmentStore = enrichmentStore;
    }

    public List<CategoryBreakdown> breakdown(List<Transaction> transactions) {
        Map<Category, BigDecimal> totals = new EnumMap<>(Category.class);

        for (Transaction tx : transactions) {
            enrichmentStore.findByTransactionId(tx.getId())
                    .ifPresent(enrichment -> {
                        if (tx.getDirection() == TransactionDirection.OUT) {
                            totals.merge(
                                    enrichment.category(),
                                    tx.getMoney().amount(),
                                    BigDecimal::add
                            );
                        }
                    });
        }

        return totals.entrySet().stream()
                .map(e -> new CategoryBreakdown(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(CategoryBreakdown::total).reversed())
                .toList();
    }
}
