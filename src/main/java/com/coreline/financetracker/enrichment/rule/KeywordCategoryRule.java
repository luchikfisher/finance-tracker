package com.coreline.financetracker.enrichment.rule;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.enrichment.model.Category;

import java.util.Map;
import java.util.Optional;

public class KeywordCategoryRule implements EnrichmentRule {

    private final Map<String, Category> keywordMapping;

    public KeywordCategoryRule(Map<String, Category> keywordMapping) {
        this.keywordMapping = keywordMapping;
    }

    @Override
    public Optional<Category> apply(Transaction transaction) {
        String text = transaction.getDescription().toLowerCase();

        return keywordMapping.entrySet().stream()
                .filter(entry -> text.contains(entry.getKey().toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public String explanation() {
        return "Matched transaction description keyword";
    }
}
