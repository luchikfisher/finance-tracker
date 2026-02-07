package com.coreline.financetracker.api.service;

import com.coreline.financetracker.api.dto.ImportSummaryDto;
import com.coreline.financetracker.common.util.Preconditions;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.service.TransactionIngestionService;
import com.coreline.financetracker.enrichment.model.EnrichmentResult;
import com.coreline.financetracker.enrichment.service.EnrichmentService;
import com.coreline.financetracker.export.service.ExportService;
import com.coreline.financetracker.importraw.model.ImportedFile;
import com.coreline.financetracker.importraw.service.RawImportService;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import com.coreline.financetracker.parsing.service.ParsingService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImportPipelineService {

    private final RawImportService rawImportService;
    private final ParsingService parsingService;
    private final TransactionIngestionService transactionIngestionService;
    private final EnrichmentService enrichmentService;
    private final ExportService exportService;

    public ImportPipelineService(
            RawImportService rawImportService,
            ParsingService parsingService,
            TransactionIngestionService transactionIngestionService,
            EnrichmentService enrichmentService,
            ExportService exportService
    ) {
        this.rawImportService = rawImportService;
        this.parsingService = parsingService;
        this.transactionIngestionService = transactionIngestionService;
        this.enrichmentService = enrichmentService;
        this.exportService = exportService;
    }

    public ImportSummaryDto importAndProcess(
            UUID userId,
            String bankName,
            String originalFilename,
            byte[] data,
            boolean export
    ) {
        Preconditions.notNull(userId, "userId is required");
        Preconditions.notBlank(bankName, "bankName is required");
        Preconditions.notBlank(originalFilename, "originalFilename is required");
        Preconditions.notNull(data, "file data is required");

        ImportedFile importedFile =
                rawImportService.importFile(userId, bankName, originalFilename, data);

        List<ParsedTransaction> parsed =
                parsingService.parse(bankName, new ByteArrayInputStream(data));

        List<Transaction> saved =
                transactionIngestionService.ingest(userId, parsed);

        List<EnrichmentResult> enrichments = new ArrayList<>();
        for (Transaction transaction : saved) {
            enrichmentService.enrich(transaction).ifPresent(enrichments::add);
        }

        if (export) {
            exportService.exportAll(userId);
        }

        List<UUID> transactionIds = saved.stream()
                .map(Transaction::getId)
                .toList();

        return new ImportSummaryDto(
                importedFile.getImportSessionId(),
                importedFile.getId(),
                importedFile.getBankName(),
                importedFile.getOriginalFilename(),
                parsed.size(),
                saved.size(),
                Math.max(parsed.size() - saved.size(), 0),
                enrichments.size(),
                export,
                transactionIds,
                Instant.now()
        );
    }
}
