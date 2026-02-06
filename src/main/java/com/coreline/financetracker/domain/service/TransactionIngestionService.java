package com.coreline.financetracker.domain.service;

import com.coreline.financetracker.common.util.Preconditions;
import com.coreline.financetracker.deduplication.model.DeduplicationResult;
import com.coreline.financetracker.deduplication.service.DeduplicationService;
import com.coreline.financetracker.domain.model.Account;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.model.TransactionDirection;
import com.coreline.financetracker.domain.repository.AccountRepository;
import com.coreline.financetracker.domain.repository.TransactionRepository;
import com.coreline.financetracker.domain.value.CurrencyCode;
import com.coreline.financetracker.parsing.model.ParsedAccountRef;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionIngestionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final DeduplicationService deduplicationService;

    public TransactionIngestionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionFactory transactionFactory,
            DeduplicationService deduplicationService
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
        this.deduplicationService = deduplicationService;
    }

    @Transactional
    public List<Transaction> ingest(List<ParsedTransaction> parsedTransactions) {
        Preconditions.notNull(parsedTransactions, "parsedTransactions must not be null");

        List<Transaction> saved = new ArrayList<>();

        for (ParsedTransaction parsed : parsedTransactions) {
            Preconditions.notNull(parsed, "parsedTransaction must not be null");

            ParsedAccountRef accountRef = parsed.account();
            Preconditions.notNull(accountRef, "account reference must not be null");

            String bankName = safeTrim(accountRef.bankName());
            String externalId = safeTrim(accountRef.accountExternalId());

            Preconditions.notBlank(bankName, "bankName is required");
            Preconditions.notBlank(externalId, "accountExternalId is required");

            Account account = accountRepository
                    .findByBankNameAndExternalAccountId(bankName, externalId)
                    .orElseGet(() -> accountRepository.save(
                            new Account(UUID.randomUUID(), bankName, externalId)
                    ));

            DeduplicationResult result =
                    deduplicationService.checkDuplicate(parsed, account.getId());

            if (result.duplicate()) {
                continue;
            }

            CurrencyCode currency = CurrencyCode.valueOf(
                    parsed.currency().trim().toUpperCase()
            );

            TransactionDirection direction =
                    TransactionDirection.valueOf(parsed.direction().name());

            Transaction transaction = transactionFactory.create(
                    account.getId(),
                    parsed.transactionDate(),
                    parsed.valueDate(),
                    parsed.amount(),
                    currency,
                    direction,
                    parsed.description(),
                    parsed.counterparty()
            );

            transactionRepository.save(transaction);
            saved.add(transaction);
        }

        return saved;
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
