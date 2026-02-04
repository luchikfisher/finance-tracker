package com.coreline.financetracker.parsing.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParsedTransaction(
        ParsedAccountRef account,
        LocalDate transactionDate,
        LocalDate valueDate,
        BigDecimal amount,
        String currency,
        ParsedDirection direction,
        String description,
        String counterparty
) {
}
