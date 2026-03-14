package com.coreline.financetracker.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminSetupRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 5, max = 254) String email,
        @NotBlank @Size(min = 6, max = 100) String password
) {
}
