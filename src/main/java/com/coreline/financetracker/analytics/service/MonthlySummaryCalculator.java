package com.coreline.financetracker.analytics.service;

import com.coreline.financetracker.analytics.model.MonthlySummary;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.model.TransactionDirection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@Service
public class MonthlySummaryCalculator {

    public List<MonthlySummary> summarize(List<Transaction> transactions) {
        Map<YearMonth, List<Transaction>> byMonth = new HashMap<>();

        for (Transaction tx : transactions) {
            YearMonth ym = YearMonth.from(tx.getTransactionDate());
            byMonth.computeIfAbsent(ym, k -> new ArrayList<>()).add(tx);
        }

        List<MonthlySummary> summaries = new ArrayList<>();

        for (Map.Entry<YearMonth, List<Transaction>> entry : byMonth.entrySet()) {
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expenses = BigDecimal.ZERO;

            for (Transaction tx : entry.getValue()) {
                if (tx.getDirection() == TransactionDirection.IN) {
                    income = income.add(tx.getMoney().amount());
                } else {
                    expenses = expenses.add(tx.getMoney().amount());
                }
            }

            summaries.add(new MonthlySummary(
                    entry.getKey(),
                    income,
                    expenses,
                    income.subtract(expenses)
            ));
        }

        summaries.sort(Comparator.comparing(MonthlySummary::month));
        return summaries;
    }
}
