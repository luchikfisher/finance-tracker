package com.coreline.financetracker.deduplication.policy;

import com.coreline.financetracker.deduplication.model.TransactionFingerprint;
import com.coreline.financetracker.domain.model.Transaction;

public class DefaultDeduplicationPolicy {

    public boolean isDuplicate(
            TransactionFingerprint fingerprint,
            Transaction existingTransaction
    ) {
        // Policy hook – future expansion
        // Currently: fingerprint match = duplicate
        return true;
    }
}
