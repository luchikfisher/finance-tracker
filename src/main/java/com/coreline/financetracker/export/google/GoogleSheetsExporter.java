package com.coreline.financetracker.export.google;

import com.coreline.financetracker.analytics.model.*;
import com.coreline.financetracker.analytics.query.TransactionQueryService;
import com.coreline.financetracker.analytics.service.*;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.export.api.Exporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "export.google.enabled", havingValue = "true")
public class GoogleSheetsExporter implements Exporter {

    private final GoogleSheetsClient client;
    private final TransactionQueryService transactionQueryService;
    private final CashFlowCalculator cashFlowCalculator;
    private final MonthlySummaryCalculator monthlySummaryCalculator;
    private final CategoryAnalyticsService categoryAnalyticsService;

    public GoogleSheetsExporter(
            GoogleSheetsClient client,
            TransactionQueryService transactionQueryService,
            CashFlowCalculator cashFlowCalculator,
            MonthlySummaryCalculator monthlySummaryCalculator,
            CategoryAnalyticsService categoryAnalyticsService
    ) {
        this.client = client;
        this.transactionQueryService = transactionQueryService;
        this.cashFlowCalculator = cashFlowCalculator;
        this.monthlySummaryCalculator = monthlySummaryCalculator;
        this.categoryAnalyticsService = categoryAnalyticsService;
    }

    @Override
    public void export(java.util.UUID userId) {
        List<Transaction> transactions =
                transactionQueryService.findAll(userId);

        exportTransactions(transactions);
        exportMonthlySummary(transactions);
        exportCategoryBreakdown(transactions);
    }

    private void exportTransactions(List<Transaction> transactions) {
        List<List<Object>> rows = new ArrayList<>();
        rows.add(new ArrayList<>(GoogleSheetsSchema.transactionsHeader()));

        for (Transaction tx : transactions) {
            rows.add(List.of(
                    tx.getId().toString(),
                    tx.getAccountId().toString(),
                    tx.getTransactionDate().toString(),
                    tx.getValueDate().toString(),
                    tx.getMoney().amount(),
                    tx.getMoney().currency().name(),
                    tx.getDirection().name(),
                    tx.getDescription(),
                    tx.getCounterparty()
            ));
        }

        client.clearAndWrite(
                GoogleSheetsSchema.TRANSACTIONS_SHEET,
                rows
        );
    }

    private void exportMonthlySummary(List<Transaction> transactions) {
        List<MonthlySummary> summaries =
                monthlySummaryCalculator.summarize(transactions);

        List<List<Object>> rows = new ArrayList<>();
        rows.add(new ArrayList<>(GoogleSheetsSchema.monthlySummaryHeader()));

        for (MonthlySummary s : summaries) {
            rows.add(List.of(
                    s.month().toString(),
                    s.income(),
                    s.expenses(),
                    s.net()
            ));
        }

        client.clearAndWrite(
                GoogleSheetsSchema.MONTHLY_SUMMARY_SHEET,
                rows
        );
    }

    private void exportCategoryBreakdown(List<Transaction> transactions) {
        List<CategoryBreakdown> breakdown =
                categoryAnalyticsService.breakdown(transactions);

        List<List<Object>> rows = new ArrayList<>();
        rows.add(new ArrayList<>(GoogleSheetsSchema.categoryBreakdownHeader()));

        for (CategoryBreakdown b : breakdown) {
            rows.add(List.of(
                    b.category().name(),
                    b.total()
            ));
        }

        client.clearAndWrite(
                GoogleSheetsSchema.CATEGORY_BREAKDOWN_SHEET,
                rows
        );
    }
}
