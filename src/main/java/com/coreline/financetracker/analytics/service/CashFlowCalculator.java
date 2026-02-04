package com.coreline.financetracker.analytics.service;

import com.coreline.financetracker.analytics.model.CashFlowSnapshot;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.domain.model.TransactionDirection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CashFlowCalculator {

    public CashFlowSnapshot calculate(List<Transaction> transactions) {
        BigDecimal in = BigDecimal.ZERO;
        BigDecimal out = BigDecimal.ZERO;

        for (Transaction tx : transactions) {
            if (tx.getDirection() == TransactionDirection.IN) {
                in = in.add(tx.getMoney().amount());
            } else {
                out = out.add(tx.getMoney().amount());
            }
        }

        return new CashFlowSnapshot(
                in,
                out,
                in.subtract(out)
        );
    }
}
