package com.coreline.financetracker.export.local;

import com.coreline.financetracker.analytics.model.CategoryBreakdown;
import com.coreline.financetracker.analytics.model.MonthlySummary;
import com.coreline.financetracker.analytics.query.TransactionQueryService;
import com.coreline.financetracker.analytics.service.CategoryAnalyticsService;
import com.coreline.financetracker.analytics.service.MonthlySummaryCalculator;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.export.api.Exporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(
        name = "export.local.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LocalCsvExporter implements Exporter {

    private final TransactionQueryService transactionQueryService;
    private final MonthlySummaryCalculator monthlySummaryCalculator;
    private final CategoryAnalyticsService categoryAnalyticsService;
    private final Path exportRoot;

    public LocalCsvExporter(
            TransactionQueryService transactionQueryService,
            MonthlySummaryCalculator monthlySummaryCalculator,
            CategoryAnalyticsService categoryAnalyticsService,
            @Value("${export.local.path:./data/exports}") String exportRoot
    ) {
        this.transactionQueryService = transactionQueryService;
        this.monthlySummaryCalculator = monthlySummaryCalculator;
        this.categoryAnalyticsService = categoryAnalyticsService;
        this.exportRoot = Path.of(exportRoot);
    }

    @Override
    public void export(java.util.UUID userId) {
        try {
            Files.createDirectories(exportRoot);

            List<Transaction> transactions = transactionQueryService.findAll(userId);

            writeTransactions(transactions);
            writeMonthlySummary(transactions);
            writeCategoryBreakdown(transactions);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to export CSV files", e);
        }
    }

    private void writeTransactions(List<Transaction> transactions) throws Exception {
        List<List<String>> rows = new ArrayList<>();
        rows.add(List.of(
                "transaction_id",
                "account_id",
                "transaction_date",
                "value_date",
                "amount",
                "currency",
                "direction",
                "description",
                "counterparty"
        ));

        for (Transaction tx : transactions) {
            rows.add(List.of(
                    tx.getId().toString(),
                    tx.getAccountId().toString(),
                    tx.getTransactionDate().toString(),
                    tx.getValueDate().toString(),
                    tx.getMoney().amount().toPlainString(),
                    tx.getMoney().currency().name(),
                    tx.getDirection().name(),
                    tx.getDescription(),
                    tx.getCounterparty() == null ? "" : tx.getCounterparty()
            ));
        }

        writeCsv(exportRoot.resolve("transactions.csv"), rows);
    }

    private void writeMonthlySummary(List<Transaction> transactions) throws Exception {
        List<MonthlySummary> summaries =
                monthlySummaryCalculator.summarize(transactions);

        List<List<String>> rows = new ArrayList<>();
        rows.add(List.of("month", "income", "expenses", "net"));

        for (MonthlySummary summary : summaries) {
            rows.add(List.of(
                    summary.month().toString(),
                    summary.income().toPlainString(),
                    summary.expenses().toPlainString(),
                    summary.net().toPlainString()
            ));
        }

        writeCsv(exportRoot.resolve("monthly_summary.csv"), rows);
    }

    private void writeCategoryBreakdown(List<Transaction> transactions) throws Exception {
        List<CategoryBreakdown> breakdown =
                categoryAnalyticsService.breakdown(transactions);

        List<List<String>> rows = new ArrayList<>();
        rows.add(List.of("category", "total"));

        for (CategoryBreakdown entry : breakdown) {
            rows.add(List.of(
                    entry.category().name(),
                    entry.total().toPlainString()
            ));
        }

        writeCsv(exportRoot.resolve("category_breakdown.csv"), rows);
    }

    private void writeCsv(Path file, List<List<String>> rows) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (List<String> row : rows) {
                writer.write(toCsvRow(row));
                writer.newLine();
            }
        }
    }

    private String toCsvRow(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escape(columns.get(i)));
        }
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean mustQuote = value.contains(",") || value.contains("\"") || value.contains("\n");
        String escaped = value.replace("\"", "\"\"");
        if (mustQuote) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
