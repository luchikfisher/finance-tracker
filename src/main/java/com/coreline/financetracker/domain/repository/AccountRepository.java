package com.coreline.financetracker.domain.repository;

import com.coreline.financetracker.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUserIdAndBankNameAndExternalAccountId(
            UUID userId,
            String bankName,
            String externalAccountId
    );

    List<Account> findByUserId(UUID userId);
}
