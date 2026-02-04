package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.export.service.ExportService;
import org.springframework.stereotype.Component;

@Component
public class ExportStep implements PipelineStep {

    private final ExportService exportService;

    public ExportStep(ExportService exportService) {
        this.exportService = exportService;
    }

    @Override
    public void execute() {
        exportService.exportAll();
    }
}
