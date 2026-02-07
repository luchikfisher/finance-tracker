package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.domain.service.TransactionIngestionService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(3)
public class DeduplicationStep implements PipelineStep {

    private final TransactionIngestionService transactionIngestionService;

    public DeduplicationStep(TransactionIngestionService transactionIngestionService) {
        this.transactionIngestionService = transactionIngestionService;
    }

    @Override
    public void execute(PipelineContext context) {
        if (context.getParsedTransactions() == null ||
                context.getParsedTransactions().isEmpty()) {
            context.setTransactions(List.of());
            return;
        }

        context.setTransactions(
                transactionIngestionService.ingest(context.getUserId(), context.getParsedTransactions())
        );
    }
}
