package com.coreline.financetracker.common.exception;

public abstract class FinanceTrackerException extends RuntimeException {

    protected FinanceTrackerException(String message) {
        super(message);
    }

    protected FinanceTrackerException(String message, Throwable cause) {
        super(message, cause);
    }
}
