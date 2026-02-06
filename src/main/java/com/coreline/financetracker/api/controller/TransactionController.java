package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.TransactionDto;
import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.repository.TransactionRepository;
import com.coreline.financetracker.analytics.query.TransactionQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionQueryService transactionQueryService;

    public TransactionController(
            TransactionRepository transactionRepository,
            TransactionQueryService transactionQueryService
    ) {
        this.transactionRepository = transactionRepository;
        this.transactionQueryService = transactionQueryService;
    }

    @GetMapping
    public List<TransactionDto> listTransactions(
            @RequestParam(value = "accountId", required = false) UUID accountId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Transaction> transactions;

        if (accountId != null && from != null && to != null) {
            transactions = transactionQueryService.findByAccountAndDateRange(accountId, from, to);
        } else if (accountId != null) {
            transactions = transactionQueryService.findByAccount(accountId);
        } else {
            transactions = transactionQueryService.findAll();
        }

        if (from != null || to != null) {
            LocalDate fromDate = from == null ? LocalDate.MIN : from;
            LocalDate toDate = to == null ? LocalDate.MAX : to;
            transactions = transactions.stream()
                    .filter(tx ->
                            !tx.getTransactionDate().isBefore(fromDate) &&
                                    !tx.getTransactionDate().isAfter(toDate)
                    )
                    .toList();
        }

        return transactions.stream()
                .map(TransactionDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public TransactionDto getTransaction(@PathVariable("id") UUID id) {
        return transactionRepository.findById(id)
                .map(TransactionDto::from)
                .orElseThrow(() -> new ValidationException("Transaction not found"));
    }
}
