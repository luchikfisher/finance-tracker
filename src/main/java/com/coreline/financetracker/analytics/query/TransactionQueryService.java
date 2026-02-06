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

    public List<Transaction> findByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findByAccountAndDateRange(
            UUID accountId,
            LocalDate from,
            LocalDate to
    ) {
        return transactionRepository.findByAccountId(accountId).stream()
                .filter(tx ->
                        !tx.getTransactionDate().isBefore(from) &&
                                !tx.getTransactionDate().isAfter(to)
                )
                .toList();
    }
}
