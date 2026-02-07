package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.RegisterRequest;
import com.coreline.financetracker.api.dto.UserDto;
import com.coreline.financetracker.user.model.AppUser;
import com.coreline.financetracker.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDto register(@Valid @RequestBody RegisterRequest request) {
        AppUser user = userService.register(request.username(), request.password());
        return UserDto.from(user);
    }
}
