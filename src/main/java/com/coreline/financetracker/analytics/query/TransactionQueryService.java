package com.coreline.financetracker.analytics.query;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public TransactionQueryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findByAccount(UUID userId, UUID accountId) {
        return transactionRepository.findByUserIdAndAccountId(userId, accountId);
    }

    public List<Transaction> findAll(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> findByAccountAndDateRange(
            UUID userId,
            UUID accountId,
            LocalDate from,
            LocalDate to
    ) {
        return transactionRepository.findByUserIdAndAccountId(userId, accountId).stream()
                .filter(tx ->
                        !tx.getTransactionDate().isBefore(from) &&
                                !tx.getTransactionDate().isAfter(to)
                )
                .toList();
    }
}
