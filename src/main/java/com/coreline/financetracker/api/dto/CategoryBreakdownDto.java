package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.analytics.model.CategoryBreakdown;

import java.math.BigDecimal;

public record CategoryBreakdownDto(
        String category,
        BigDecimal total
) {
    public static CategoryBreakdownDto from(CategoryBreakdown breakdown) {
        return new CategoryBreakdownDto(
                breakdown.category().name(),
                breakdown.total()
        );
    }
}
