package com.coreline.financetracker.export.google;

import java.util.List;

public final class GoogleSheetsSchema {

    private GoogleSheetsSchema() {}

    public static final String TRANSACTIONS_SHEET = "Transactions";
    public static final String MONTHLY_SUMMARY_SHEET = "Monthly Summary";
    public static final String CATEGORY_BREAKDOWN_SHEET = "Category Breakdown";

    public static List<String> transactionsHeader() {
        return List.of(
                "Transaction ID",
                "Account ID",
                "Transaction Date",
                "Value Date",
                "Amount",
                "Currency",
                "Direction",
                "Description",
                "Counterparty"
        );
    }

    public static List<String> monthlySummaryHeader() {
        return List.of(
                "Month",
                "Income",
                "Expenses",
                "Net"
        );
    }

    public static List<String> categoryBreakdownHeader() {
        return List.of(
                "Category",
                "Total"
        );
    }
}