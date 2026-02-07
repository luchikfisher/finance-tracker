package com.coreline.financetracker.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_accounts_user_bank_external",
                        columnNames = {"user_id", "bank_name", "external_account_id"}
                )
        }
)
public class Account {

    @Id
    private UUID id;

    @Column
    private UUID userId;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String externalAccountId;

    protected Account() {
        // JPA
    }

    public Account(UUID id, UUID userId, String bankName, String externalAccountId) {
        this.id = id;
        this.userId = userId;
        this.bankName = bankName;
        this.externalAccountId = externalAccountId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getExternalAccountId() {
        return externalAccountId;
    }
}
