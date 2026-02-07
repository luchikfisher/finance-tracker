package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.UpdateRoleRequest;
import com.coreline.financetracker.api.dto.UserDto;
import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.model.UserRole;
import com.coreline.financetracker.user.service.CurrentUserService;
import com.coreline.financetracker.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    public UserController(
            UserService userService,
            CurrentUserService currentUserService
    ) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public UserDto me() {
        return UserDto.from(currentUserService.requireUser());
    }

    @GetMapping
    @PreAuthorize("@access.isAdmin(authentication)")
    public List<UserDto> listUsers() {
        return userService.list().stream()
                .map(UserDto::from)
                .toList();
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("@access.isAdmin(authentication)")
    public UserDto updateRole(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        UserRole role;
        try {
            role = UserRole.valueOf(request.role().trim().toUpperCase());
        } catch (Exception e) {
            throw new ValidationException("Invalid role");
        }
        AppUser updated = userService.changeRole(id, role);
        return UserDto.from(updated);
    }
}
