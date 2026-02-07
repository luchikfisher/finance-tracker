package com.coreline.financetracker.user.service;

import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.security.JwtService;
import com.coreline.financetracker.security.TokenGenerator;
import com.coreline.financetracker.security.TokenHasher;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.model.PasswordResetToken;
import com.coreline.financetracker.user.model.RefreshToken;
import com.coreline.financetracker.user.repository.PasswordResetTokenRepository;
import com.coreline.financetracker.user.repository.RefreshTokenRepository;
import com.coreline.financetracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenGenerator tokenGenerator;
    private final long refreshTtlDays;
    private final long resetTtlMinutes;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TokenGenerator tokenGenerator,
            @Value("${security.jwt.refresh-ttl-days:30}") long refreshTtlDays,
            @Value("${security.reset.ttl-minutes:30}") long resetTtlMinutes
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenGenerator = tokenGenerator;
        this.refreshTtlDays = refreshTtlDays;
        this.resetTtlMinutes = resetTtlMinutes;
    }

    @Transactional
    public TokenPair login(String username, String password) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ValidationException("Invalid credentials");
        }
        return issueTokens(user);
    }

    @Transactional
    public TokenPair refresh(String refreshToken) {
        String hash = TokenHasher.sha256(refreshToken);
        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ValidationException("Invalid refresh token"));

        Instant now = Instant.now();
        if (!stored.isActive(now)) {
            throw new ValidationException("Refresh token expired or revoked");
        }

        AppUser user = userRepository.findById(stored.getUserId())
                .orElseThrow(() -> new ValidationException("User not found"));

        stored.revoke(now);
        refreshTokenRepository.save(stored);

        return issueTokens(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        String hash = TokenHasher.sha256(refreshToken);
        refreshTokenRepository.findByTokenHash(hash)
                .ifPresent(token -> {
                    token.revoke(Instant.now());
                    refreshTokenRepository.save(token);
                });
    }

    @Transactional
    public Optional<String> requestPasswordReset(String username) {
        Optional<AppUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        AppUser user = userOpt.get();
        String raw = tokenGenerator.generate();
        String hash = TokenHasher.sha256(raw);
        Instant now = Instant.now();
        Instant exp = now.plus(resetTtlMinutes, ChronoUnit.MINUTES);

        PasswordResetToken token = new PasswordResetToken(
                UUID.randomUUID(),
                user.getId(),
                hash,
                exp,
                now,
                null
        );
        passwordResetTokenRepository.save(token);
        return Optional.of(raw);
    }

    @Transactional
    public void confirmPasswordReset(String username, String token, String newPassword) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Invalid reset token"));

        String hash = TokenHasher.sha256(token);
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ValidationException("Invalid reset token"));

        Instant now = Instant.now();
        if (!resetToken.isActive(now) || !resetToken.getUserId().equals(user.getId())) {
            throw new ValidationException("Invalid reset token");
        }

        user.updatePasswordHash(passwordEncoder.encode(newPassword), now);
        userRepository.save(user);

        resetToken.markUsed(now);
        passwordResetTokenRepository.save(resetToken);
    }

    private TokenPair issueTokens(AppUser user) {
        String accessToken = jwtService.generateAccessToken(user);

        String refreshRaw = tokenGenerator.generate();
        String refreshHash = TokenHasher.sha256(refreshRaw);
        Instant now = Instant.now();
        Instant exp = now.plus(refreshTtlDays, ChronoUnit.DAYS);

        RefreshToken refresh = new RefreshToken(
                UUID.randomUUID(),
                user.getId(),
                refreshHash,
                exp,
                now,
                null
        );
        refreshTokenRepository.save(refresh);

        return new TokenPair(accessToken, refreshRaw, exp);
    }

    public record TokenPair(String accessToken, String refreshToken, Instant refreshExpiresAt) {
    }
}
