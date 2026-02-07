package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.AccountDto;
import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.domain.repository.AccountRepository;
import com.coreline.financetracker.user.service.CurrentUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final CurrentUserService currentUserService;

    public AccountController(
            AccountRepository accountRepository,
            CurrentUserService currentUserService
    ) {
        this.accountRepository = accountRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    @PreAuthorize("@access.canRead(authentication)")
    public List<AccountDto> listAccounts() {
        return accountRepository.findByUserId(currentUserService.requireUserId()).stream()
                .map(AccountDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@access.canRead(authentication)")
    public AccountDto getAccount(@PathVariable("id") UUID id) {
        return accountRepository.findById(id)
                .filter(account -> currentUserService.requireUserId().equals(account.getUserId()))
                .map(AccountDto::from)
                .orElseThrow(() -> new ValidationException("Account not found"));
    }
}
