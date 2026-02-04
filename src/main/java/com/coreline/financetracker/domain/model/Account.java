package com.coreline.financetracker.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_accounts_bank_external",
                        columnNames = {"bank_name", "external_account_id"}
                )
        }
)
public class Account {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String externalAccountId;

    protected Account() {
        // JPA
    }

    public Account(UUID id, String bankName, String externalAccountId) {
        this.id = id;
        this.bankName = bankName;
        this.externalAccountId = externalAccountId;
    }

    public UUID getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public String getExternalAccountId() {
        return externalAccountId;
    }
}
