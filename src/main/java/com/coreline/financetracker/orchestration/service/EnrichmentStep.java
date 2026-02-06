package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.enrichment.model.EnrichmentResult;
import com.coreline.financetracker.enrichment.service.EnrichmentService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(4)
public class EnrichmentStep implements PipelineStep {

    private final EnrichmentService enrichmentService;

    public EnrichmentStep(EnrichmentService enrichmentService) {
        this.enrichmentService = enrichmentService;
    }

    @Override
    public void execute(PipelineContext context) {
        if (context.getTransactions() == null ||
                context.getTransactions().isEmpty()) {
            context.setEnrichmentResults(List.of());
            return;
        }

        List<EnrichmentResult> results = new ArrayList<>();

        for (Transaction transaction : context.getTransactions()) {
            enrichmentService.enrich(transaction).ifPresent(results::add);
        }

        context.setEnrichmentResults(results);
    }
}
