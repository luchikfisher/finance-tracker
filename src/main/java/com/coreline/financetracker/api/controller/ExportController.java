package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.ExportRunDto;
import com.coreline.financetracker.export.service.ExportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/exports")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("/run")
    public ExportRunDto run() {
        exportService.exportAll();
        return new ExportRunDto(true, "Export completed", Instant.now());
    }
}
