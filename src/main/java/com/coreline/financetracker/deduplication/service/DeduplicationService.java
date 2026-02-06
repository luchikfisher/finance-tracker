package com.coreline.financetracker.deduplication.service;

import com.coreline.financetracker.deduplication.model.*;
import com.coreline.financetracker.deduplication.policy.DefaultDeduplicationPolicy;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.repository.TransactionRepository;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import com.coreline.financetracker.common.util.Preconditions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeduplicationService {

    private final FingerprintService fingerprintService;
    private final TransactionRepository transactionRepository;
    private final DefaultDeduplicationPolicy policy = new DefaultDeduplicationPolicy();

    public DeduplicationService(
            FingerprintService fingerprintService,
            TransactionRepository transactionRepository
    ) {
        this.fingerprintService = fingerprintService;
        this.transactionRepository = transactionRepository;
    }

    public DeduplicationResult checkDuplicate(
            ParsedTransaction parsedTransaction,
            UUID accountId
    ) {
        Preconditions.notNull(parsedTransaction, "parsedTransaction must not be null");
        Preconditions.notNull(accountId, "accountId must not be null");

        TransactionFingerprint fingerprint =
                fingerprintService.fingerprint(parsedTransaction, accountId);

        List<Transaction> existing =
                transactionRepository.findByAccountId(accountId);

        for (Transaction tx : existing) {
            TransactionFingerprint existingFingerprint =
                    fingerprintService.fingerprint(tx);

            if (policy.isDuplicate(fingerprint, existingFingerprint)) {
                return new DeduplicationResult(tx.getId(), true);
            }
        }

        return new DeduplicationResult(null, false);
    }
}
