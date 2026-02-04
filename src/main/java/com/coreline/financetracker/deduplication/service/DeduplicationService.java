package com.coreline.financetracker.deduplication.service;

import com.coreline.financetracker.deduplication.model.*;
import com.coreline.financetracker.deduplication.policy.DefaultDeduplicationPolicy;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.repository.TransactionRepository;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
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
        TransactionFingerprint fingerprint =
                fingerprintService.fingerprint(parsedTransaction);

        List<Transaction> existing =
                transactionRepository.findByAccountId(accountId);

        for (Transaction tx : existing) {
            if (policy.isDuplicate(fingerprint, tx)) {
                return new DeduplicationResult(tx.getId(), true);
            }
        }

        return new DeduplicationResult(null, false);
    }
}
