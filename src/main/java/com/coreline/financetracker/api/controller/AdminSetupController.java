package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.AdminSetupRequest;
import com.coreline.financetracker.api.dto.UserDto;
import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.repository.UserRepository;
import com.coreline.financetracker.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminSetupController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AdminSetupController(
            UserRepository userRepository,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping(
            path = "/setup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDto setup(@Valid @RequestBody AdminSetupRequest request) {
        if (userRepository.count() > 0) {
            throw new ValidationException("Admin setup already completed");
        }
        AppUser admin = userService.createAdmin(request.username(), request.password());
        return UserDto.from(admin);
    }
}
