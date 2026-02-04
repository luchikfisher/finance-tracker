package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.importraw.service.RawImportService;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class RawImportStep implements PipelineStep {

    private final RawImportService rawImportService;

    public RawImportStep(RawImportService rawImportService) {
        this.rawImportService = rawImportService;
    }

    @Override
    public void execute() {
        // Placeholder input – orchestration controls inputs
        InputStream inputStream = InputStream.nullInputStream();

        rawImportService.importFile(
                "DUMMY_BANK",
                "import.csv",
                inputStream
        );
    }
}