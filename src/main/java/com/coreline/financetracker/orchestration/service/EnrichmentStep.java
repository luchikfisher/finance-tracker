package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.enrichment.service.EnrichmentService;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentStep implements PipelineStep {

    private final EnrichmentService enrichmentService;

    public EnrichmentStep(EnrichmentService enrichmentService) {
        this.enrichmentService = enrichmentService;
    }

    @Override
    public void execute() {
        // Placeholder – orchestration injects actual transactions
        Transaction transaction = null;

        enrichmentService.enrich(transaction);
    }
}
