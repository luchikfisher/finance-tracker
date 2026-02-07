package com.coreline.financetracker.domain.model;

import com.coreline.financetracker.domain.value.Money;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transactions_account_date", columnList = "account_id, transaction_date")
        }
)
public class Transaction {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @Column
    private UUID userId;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private LocalDate valueDate;

    @Embedded
    private Money money;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionDirection direction;

    @Column(nullable = false)
    private String description;

    @Column
    private String counterparty;

    protected Transaction() {
        // JPA
    }

    public Transaction(
            UUID id,
            UUID accountId,
            UUID userId,
            LocalDate transactionDate,
            LocalDate valueDate,
            Money money,
            TransactionDirection direction,
            String description,
            String counterparty
    ) {
        this.id = id;
        this.accountId = accountId;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.valueDate = valueDate;
        this.money = money;
        this.direction = direction;
        this.description = description;
        this.counterparty = counterparty;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public Money getMoney() {
        return money;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public String getDescription() {
        return description;
    }

    public String getCounterparty() {
        return counterparty;
    }
}
