package com.coreline.financetracker.domain.service;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.model.TransactionDirection;
import com.coreline.financetracker.domain.value.CurrencyCode;
import com.coreline.financetracker.domain.value.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class TransactionFactory {

    public Transaction create(
            UUID accountId,
            UUID userId,
            LocalDate transactionDate,
            LocalDate valueDate,
            BigDecimal amount,
            CurrencyCode currency,
            TransactionDirection direction,
            String description,
            String counterparty
    ) {
        Money money = new Money(amount.abs(), currency);

        return new Transaction(
                UUID.randomUUID(),
                accountId,
                userId,
                transactionDate,
                valueDate,
                money,
                direction,
                description,
                counterparty
        );
    }
}
