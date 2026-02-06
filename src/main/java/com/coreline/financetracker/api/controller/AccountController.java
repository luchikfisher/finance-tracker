package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.AccountDto;
import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.domain.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<AccountDto> listAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public AccountDto getAccount(@PathVariable("id") UUID id) {
        return accountRepository.findById(id)
                .map(AccountDto::from)
                .orElseThrow(() -> new ValidationException("Account not found"));
    }
}
