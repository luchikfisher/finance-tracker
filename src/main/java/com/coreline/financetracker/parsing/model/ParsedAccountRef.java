package com.coreline.financetracker.parsing.model;

public record ParsedAccountRef(
        String bankName,
        String accountExternalId
) {
}