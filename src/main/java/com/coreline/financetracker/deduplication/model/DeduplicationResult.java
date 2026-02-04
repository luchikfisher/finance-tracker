package com.coreline.financetracker.deduplication.model;

import java.util.UUID;

public record DeduplicationResult(
        UUID transactionId,
        boolean duplicate
) {
}
