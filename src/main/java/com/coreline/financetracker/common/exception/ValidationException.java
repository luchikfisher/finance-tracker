package com.coreline.financetracker.common.exception;

public class ValidationException extends FinanceTrackerException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
