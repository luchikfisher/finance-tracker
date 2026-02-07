package com.coreline.financetracker.user.service;

import com.coreline.financetracker.common.time.ClockProvider;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.model.UserRole;
import com.coreline.financetracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Component
public class UserBootstrap {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClockProvider clockProvider;
    private final String adminUsername;
    private final String adminPassword;

    public UserBootstrap(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ClockProvider clockProvider,
            @Value("${admin.bootstrap.username:}") String adminUsername,
            @Value("${admin.bootstrap.password:}") String adminPassword
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clockProvider = clockProvider;
        this.adminUsername = adminUsername == null ? "" : adminUsername.trim();
        this.adminPassword = adminPassword == null ? "" : adminPassword.trim();
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            return;
        }
        if (adminUsername.isBlank() || adminPassword.isBlank()) {
            System.err.println("No users exist. Set admin.bootstrap.username/password to create the first admin.");
            return;
        }

        AppUser admin = new AppUser(
                UUID.randomUUID(),
                adminUsername,
                passwordEncoder.encode(adminPassword),
                UserRole.SYSTEM_ADMIN,
                clockProvider.now(),
                clockProvider.now()
        );
        userRepository.save(admin);
        System.out.println("Bootstrapped admin user: " + adminUsername);
    }
}
