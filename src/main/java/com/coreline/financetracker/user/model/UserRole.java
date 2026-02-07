package com.coreline.financetracker.user.model;

public enum UserRole {
    ACTIVE_USER,
    READ_ONLY,
    READ_WRITE,
    FULL_ACCESS,
    SYSTEM_ADMIN;

    public boolean canRead() {
        return switch (this) {
            case ACTIVE_USER, READ_ONLY, READ_WRITE, FULL_ACCESS, SYSTEM_ADMIN -> true;
        };
    }

    public boolean canWrite() {
        return switch (this) {
            case READ_WRITE, FULL_ACCESS, SYSTEM_ADMIN -> true;
            case ACTIVE_USER, READ_ONLY -> false;
        };
    }

    public boolean canDelete() {
        return switch (this) {
            case FULL_ACCESS, SYSTEM_ADMIN -> true;
            default -> false;
        };
    }

    public boolean isAdmin() {
        return this == SYSTEM_ADMIN;
    }
}
