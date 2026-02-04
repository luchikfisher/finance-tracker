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
    public ParsedTransaction {
        if (account == null) throw new IllegalArgumentException("account is required");
        if (transactionDate == null) throw new IllegalArgumentException("transactionDate is required");
        if (valueDate == null) throw new IllegalArgumentException("valueDate is required");
        if (amount == null) throw new IllegalArgumentException("amount is required");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("currency is required");
        if (direction == null) throw new IllegalArgumentException("direction is required");
        if (description == null) description = "";
        if (counterparty == null) counterparty = "";
    }
}
