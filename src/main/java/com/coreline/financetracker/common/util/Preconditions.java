package com.coreline.financetracker.common.util;

import com.coreline.financetracker.common.exception.ValidationException;

public final class Preconditions {

    private Preconditions() {}

    public static void notNull(Object value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
    }

    public static void notBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(message);
        }
    }

    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }
}