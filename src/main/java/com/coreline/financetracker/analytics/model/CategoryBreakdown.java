package com.coreline.financetracker.analytics.model;

import com.coreline.financetracker.enrichment.model.Category;

import java.math.BigDecimal;

public record CategoryBreakdown(
        Category category,
        BigDecimal total
) {
}
