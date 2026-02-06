package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.analytics.model.CashFlowSnapshot;

import java.math.BigDecimal;

public record CashFlowSnapshotDto(
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal net
) {
    public static CashFlowSnapshotDto from(CashFlowSnapshot snapshot) {
        return new CashFlowSnapshotDto(
                snapshot.totalIn(),
                snapshot.totalOut(),
                snapshot.net()
        );
    }
}
