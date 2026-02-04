package com.coreline.financetracker.domain.value;

import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.common.util.Preconditions;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class Money {

    @Column(nullable = false, precision = 18, scale = AppConstants.MONEY_SCALE)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    protected Money() {
        // JPA
    }

    public Money(BigDecimal amount, CurrencyCode currency) {
        Preconditions.notNull(amount, "amount must not be null");
        Preconditions.notNull(currency, "currency must not be null");

        this.amount = amount.setScale(AppConstants.MONEY_SCALE, RoundingMode.HALF_UP);
        this.currency = currency.name();
    }

    public BigDecimal amount() {
        return amount;
    }

    public CurrencyCode currency() {
        return CurrencyCode.valueOf(currency);
    }

    public Money negate() {
        return new Money(amount.negate(), currency());
    }
}