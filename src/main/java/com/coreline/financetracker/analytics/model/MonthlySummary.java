package com.coreline.financetracker.analytics.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlySummary(
        YearMonth month,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal net
) {
}