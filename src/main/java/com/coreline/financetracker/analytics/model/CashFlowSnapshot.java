package com.coreline.financetracker.analytics.model;

import java.math.BigDecimal;

public record CashFlowSnapshot(
        BigDecimal totalIn,
        BigDecimal totalOut,
        BigDecimal net
) {
}