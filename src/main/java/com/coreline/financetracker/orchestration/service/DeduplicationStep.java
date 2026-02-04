package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.deduplication.service.DeduplicationService;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeduplicationStep implements PipelineStep {

    private final DeduplicationService deduplicationService;

    public DeduplicationStep(DeduplicationService deduplicationService) {
        this.deduplicationService = deduplicationService;
    }

    @Override
    public void execute() {
        // Placeholder – real parsed transactions will be passed later
        ParsedTransaction parsedTransaction = null;

        deduplicationService.checkDuplicate(
                parsedTransaction,
                UUID.randomUUID()
        );
    }
}
