package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.HealthDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public HealthDto health() {
        return new HealthDto("ok", Instant.now());
    }
}
