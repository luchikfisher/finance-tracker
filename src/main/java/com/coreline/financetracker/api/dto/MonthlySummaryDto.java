package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.analytics.model.MonthlySummary;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlySummaryDto(
        YearMonth month,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal net
) {
    public static MonthlySummaryDto from(MonthlySummary summary) {
        return new MonthlySummaryDto(
                summary.month(),
                summary.income(),
                summary.expenses(),
                summary.net()
        );
    }
}
