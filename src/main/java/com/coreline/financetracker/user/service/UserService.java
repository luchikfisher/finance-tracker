package com.coreline.financetracker.user.service;

import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.common.time.ClockProvider;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.model.UserRole;
import com.coreline.financetracker.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClockProvider clockProvider;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ClockProvider clockProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clockProvider = clockProvider;
    }

    public AppUser register(String username, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("password is required");
        }

        userRepository.findByUsername(username)
                .ifPresent(existing -> {
                    throw new ValidationException("username already exists");
                });

        String hash = passwordEncoder.encode(rawPassword);
        AppUser user = new AppUser(
                UUID.randomUUID(),
                username.trim(),
                hash,
                UserRole.ACTIVE_USER,
                clockProvider.now(),
                clockProvider.now()
        );
        return userRepository.save(user);
    }

    public AppUser createAdmin(String username, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("password is required");
        }

        userRepository.findByUsername(username)
                .ifPresent(existing -> {
                    throw new ValidationException("username already exists");
                });

        String hash = passwordEncoder.encode(rawPassword);
        AppUser user = new AppUser(
                UUID.randomUUID(),
                username.trim(),
                hash,
                UserRole.SYSTEM_ADMIN,
                clockProvider.now(),
                clockProvider.now()
        );
        return userRepository.save(user);
    }

    public AppUser requireById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("User not found"));
    }

    public AppUser requireByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("User not found"));
    }

    public List<AppUser> list() {
        return userRepository.findAll();
    }

    public AppUser changeRole(UUID userId, UserRole role) {
        AppUser user = requireById(userId);
        user.updateRole(role, clockProvider.now());
        return userRepository.save(user);
    }
}
