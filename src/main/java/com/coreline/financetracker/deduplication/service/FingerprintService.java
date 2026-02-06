package com.coreline.financetracker.deduplication.service;

import com.coreline.financetracker.common.util.Preconditions;
import com.coreline.financetracker.deduplication.model.TransactionFingerprint;
import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class FingerprintService {

    public TransactionFingerprint fingerprint(ParsedTransaction tx) {
        Preconditions.notNull(tx, "parsed transaction must not be null");

        String bankName = tx.account() == null ? null : tx.account().bankName();
        String accountExternalId = tx.account() == null ? null : tx.account().accountExternalId();

        return fingerprint(
                bankName,
                accountExternalId,
                tx.transactionDate(),
                tx.valueDate(),
                tx.amount(),
                tx.currency(),
                tx.direction() == null ? null : tx.direction().name(),
                tx.description(),
                tx.counterparty()
        );
    }

    public TransactionFingerprint fingerprint(ParsedTransaction tx, UUID accountId) {
        Preconditions.notNull(tx, "parsed transaction must not be null");
        Preconditions.notNull(accountId, "accountId must not be null");

        return fingerprint(
                null,
                accountId.toString(),
                tx.transactionDate(),
                tx.valueDate(),
                tx.amount(),
                tx.currency(),
                tx.direction() == null ? null : tx.direction().name(),
                tx.description(),
                tx.counterparty()
        );
    }

    public TransactionFingerprint fingerprint(Transaction tx) {
        Preconditions.notNull(tx, "transaction must not be null");

        return fingerprint(
                null,
                tx.getAccountId() == null ? null : tx.getAccountId().toString(),
                tx.getTransactionDate(),
                tx.getValueDate(),
                tx.getMoney() == null ? null : tx.getMoney().amount(),
                tx.getMoney() == null ? null : tx.getMoney().currency().name(),
                tx.getDirection() == null ? null : tx.getDirection().name(),
                tx.getDescription(),
                tx.getCounterparty()
        );
    }

    private TransactionFingerprint fingerprint(
            String bankName,
            String accountExternalId,
            LocalDate transactionDate,
            LocalDate valueDate,
            BigDecimal amount,
            String currency,
            String direction,
            String description,
            String counterparty
    ) {
        try {
            String raw = String.join("|",
                    normalize(bankName),
                    normalize(accountExternalId),
                    safeDate(transactionDate),
                    safeDate(valueDate),
                    safeAmount(amount),
                    normalize(currency),
                    normalize(direction),
                    normalize(description),
                    normalize(counterparty)
            );

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return new TransactionFingerprint(toHex(hash));

        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate transaction fingerprint", e);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    private String safeDate(LocalDate value) {
        return value == null ? "" : value.toString();
    }

    private String safeAmount(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
