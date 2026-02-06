package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.domain.model.Account;

import java.util.UUID;

public record AccountDto(
        UUID id,
        String bankName,
        String externalAccountId
) {
    public static AccountDto from(Account account) {
        return new AccountDto(
                account.getId(),
                account.getBankName(),
                account.getExternalAccountId()
        );
    }
}
