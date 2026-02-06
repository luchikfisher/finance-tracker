package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.export.service.ExportService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class ExportStep implements PipelineStep {

    private final ExportService exportService;

    public ExportStep(ExportService exportService) {
        this.exportService = exportService;
    }

    @Override
    public void execute(PipelineContext context) {
        exportService.exportAll();
    }
}
