package com.coreline.financetracker.domain.value;

import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.common.util.Preconditions;
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
        Preconditions.notNull(amount, "amount must not be null");
        Preconditions.notNull(currency, "currency must not be null");

        amount.setScale(AppConstants.MONEY_SCALE, RoundingMode.HALF_UP);
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
