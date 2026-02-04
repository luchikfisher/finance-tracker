package com.coreline.financetracker.enrichment.rule;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.enrichment.model.Category;

import java.util.Optional;

public interface EnrichmentRule {

    Optional<Category> apply(Transaction transaction);

    String explanation();
}
