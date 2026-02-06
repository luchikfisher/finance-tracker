package com.coreline.financetracker.deduplication.policy;

import com.coreline.financetracker.deduplication.model.TransactionFingerprint;
public class DefaultDeduplicationPolicy {

    public boolean isDuplicate(
            TransactionFingerprint incoming,
            TransactionFingerprint existing
    ) {
        // Policy hook – future expansion
        // Currently: fingerprint match = duplicate
        return incoming.equals(existing);
    }
}
