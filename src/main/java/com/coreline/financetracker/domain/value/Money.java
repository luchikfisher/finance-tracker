package com.coreline.financetracker.domain.value;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public class Money {

    private BigDecimal amount;
    private String currency;

    protected Money() {
        // JPA
    }

    public Money(BigDecimal amount, CurrencyCode currency) {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
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
