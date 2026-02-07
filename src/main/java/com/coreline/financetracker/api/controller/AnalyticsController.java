package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.CashFlowSnapshotDto;
import com.coreline.financetracker.api.dto.CategoryBreakdownDto;
import com.coreline.financetracker.api.dto.MonthlySummaryDto;
import com.coreline.financetracker.analytics.model.CashFlowSnapshot;
import com.coreline.financetracker.analytics.service.CashFlowCalculator;
import com.coreline.financetracker.analytics.service.CategoryAnalyticsService;
import com.coreline.financetracker.analytics.service.MonthlySummaryCalculator;
import com.coreline.financetracker.analytics.query.TransactionQueryService;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.user.service.CurrentUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final TransactionQueryService transactionQueryService;
    private final CashFlowCalculator cashFlowCalculator;
    private final MonthlySummaryCalculator monthlySummaryCalculator;
    private final CategoryAnalyticsService categoryAnalyticsService;
    private final CurrentUserService currentUserService;

    public AnalyticsController(
            TransactionQueryService transactionQueryService,
            CashFlowCalculator cashFlowCalculator,
            MonthlySummaryCalculator monthlySummaryCalculator,
            CategoryAnalyticsService categoryAnalyticsService,
            CurrentUserService currentUserService
    ) {
        this.transactionQueryService = transactionQueryService;
        this.cashFlowCalculator = cashFlowCalculator;
        this.monthlySummaryCalculator = monthlySummaryCalculator;
        this.categoryAnalyticsService = categoryAnalyticsService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/cashflow")
    @PreAuthorize("@access.canRead(authentication)")
    public CashFlowSnapshotDto cashFlow(
            @RequestParam(value = "accountId", required = false) UUID accountId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Transaction> transactions = loadTransactions(accountId, from, to);
        CashFlowSnapshot snapshot = cashFlowCalculator.calculate(transactions);
        return CashFlowSnapshotDto.from(snapshot);
    }

    @GetMapping("/monthly-summary")
    @PreAuthorize("@access.canRead(authentication)")
    public List<MonthlySummaryDto> monthlySummary(
            @RequestParam(value = "accountId", required = false) UUID accountId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Transaction> transactions = loadTransactions(accountId, from, to);
        return monthlySummaryCalculator.summarize(transactions).stream()
                .map(MonthlySummaryDto::from)
                .toList();
    }

    @GetMapping("/category-breakdown")
    @PreAuthorize("@access.canRead(authentication)")
    public List<CategoryBreakdownDto> categoryBreakdown(
            @RequestParam(value = "accountId", required = false) UUID accountId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Transaction> transactions = loadTransactions(accountId, from, to);
        return categoryAnalyticsService.breakdown(transactions).stream()
                .map(CategoryBreakdownDto::from)
                .toList();
    }

    private List<Transaction> loadTransactions(
            UUID accountId,
            LocalDate from,
            LocalDate to
    ) {
        UUID userId = currentUserService.requireUserId();
        List<Transaction> transactions;

        if (accountId != null && from != null && to != null) {
            transactions = transactionQueryService.findByAccountAndDateRange(userId, accountId, from, to);
        } else if (accountId != null) {
            transactions = transactionQueryService.findByAccount(userId, accountId);
        } else {
            transactions = transactionQueryService.findAll(userId);
        }

        if (from != null || to != null) {
            LocalDate fromDate = from == null ? LocalDate.MIN : from;
            LocalDate toDate = to == null ? LocalDate.MAX : to;
            return transactions.stream()
                    .filter(tx ->
                            !tx.getTransactionDate().isBefore(fromDate) &&
                                    !tx.getTransactionDate().isAfter(toDate)
                    )
                    .toList();
        }

        return transactions;
    }
}
