package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionDto(
        UUID id,
        UUID accountId,
        LocalDate transactionDate,
        LocalDate valueDate,
        BigDecimal amount,
        String currency,
        String direction,
        String description,
        String counterparty
) {
    public static TransactionDto from(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionDate(),
                transaction.getValueDate(),
                transaction.getMoney().amount(),
                transaction.getMoney().currency().name(),
                transaction.getDirection().name(),
                transaction.getDescription(),
                transaction.getCounterparty()
        );
    }
}
