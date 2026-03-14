package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.user.model.AppUser;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String role,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserDto from(AppUser user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
