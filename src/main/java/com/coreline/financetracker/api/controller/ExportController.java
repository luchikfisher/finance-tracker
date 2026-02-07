package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.ExportRunDto;
import com.coreline.financetracker.export.service.ExportService;
import com.coreline.financetracker.user.service.CurrentUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/exports")
public class ExportController {

    private final ExportService exportService;
    private final CurrentUserService currentUserService;

    public ExportController(
            ExportService exportService,
            CurrentUserService currentUserService
    ) {
        this.exportService = exportService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/run")
    @PreAuthorize("@access.canWrite(authentication)")
    public ExportRunDto run() {
        exportService.exportAll(currentUserService.requireUserId());
        return new ExportRunDto(true, "Export completed", Instant.now());
    }
}
