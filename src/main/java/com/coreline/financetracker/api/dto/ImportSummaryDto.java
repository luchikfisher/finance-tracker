package com.coreline.financetracker.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ImportSummaryDto(
        UUID importSessionId,
        UUID importedFileId,
        String bankName,
        String originalFilename,
        int parsedCount,
        int savedCount,
        int duplicatesCount,
        int enrichedCount,
        boolean exportTriggered,
        List<UUID> transactionIds,
        Instant completedAt
) {
}
