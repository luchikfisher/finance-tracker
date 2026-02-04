package com.coreline.financetracker.deduplication.model;

import java.util.Objects;

public final class TransactionFingerprint {

    private final String value;

    public TransactionFingerprint(String value) {
        this.value = Objects.requireNonNull(value, "fingerprint value must not be null");
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionFingerprint)) return false;
        TransactionFingerprint that = (TransactionFingerprint) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
