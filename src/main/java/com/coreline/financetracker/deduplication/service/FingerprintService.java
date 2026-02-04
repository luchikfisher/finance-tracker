package com.coreline.financetracker.deduplication.service;

import com.coreline.financetracker.deduplication.model.TransactionFingerprint;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class FingerprintService {

    public TransactionFingerprint fingerprint(ParsedTransaction tx) {
        try {
            String raw = String.join("|",
                    tx.account().bankName(),
                    tx.account().accountExternalId(),
                    tx.transactionDate().toString(),
                    tx.valueDate().toString(),
                    tx.amount().toPlainString(),
                    tx.currency(),
                    tx.direction().name(),
                    normalize(tx.description()),
                    normalize(tx.counterparty())
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

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
