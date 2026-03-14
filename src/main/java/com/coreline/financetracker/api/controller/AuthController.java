package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.*;
import com.coreline.financetracker.user.service.AuthService;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final long accessTtlSeconds;
    private final boolean returnResetToken;

    public AuthController(
            UserService userService,
            AuthService authService,
            @Value("${security.jwt.access-ttl-minutes:30}") long accessTtlMinutes,
            @Value("${security.reset.return-token:true}") boolean returnResetToken
    ) {
        this.userService = userService;
        this.authService = authService;
        this.accessTtlSeconds = accessTtlMinutes * 60;
        this.returnResetToken = returnResetToken;
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDto register(@Valid @RequestBody RegisterRequest request) {
        AppUser user = userService.register(
                request.username(),
                request.email(),
                request.password()
        );
        return UserDto.from(user);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        AuthService.TokenPair pair = authService.login(request.username(), request.password());
        return new TokenResponse(
                pair.accessToken(),
                pair.refreshToken(),
                "Bearer",
                accessTtlSeconds,
                pair.refreshExpiresAt()
        );
    }

    @PostMapping(
            path = "/refresh",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        AuthService.TokenPair pair = authService.refresh(request.refreshToken());
        return new TokenResponse(
                pair.accessToken(),
                pair.refreshToken(),
                "Bearer",
                accessTtlSeconds,
                pair.refreshExpiresAt()
        );
    }

    @PostMapping(
            path = "/logout",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SimpleMessage logout(@Valid @RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
        return new SimpleMessage("Logged out");
    }

    @PostMapping(
            path = "/password-reset/request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PasswordResetResponse requestReset(@Valid @RequestBody PasswordResetRequest request) {
        String token = authService.requestPasswordReset(request.email()).orElse(null);
        if (!returnResetToken) {
            token = null;
        }
        return new PasswordResetResponse("If the user exists, a reset token has been issued", token);
    }

    @PostMapping(
            path = "/password-reset/confirm",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SimpleMessage confirmReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        authService.confirmPasswordReset(
                request.email(),
                request.resetToken(),
                request.newPassword()
        );
        return new SimpleMessage("Password updated");
    }
}
