package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.importraw.service.RawImportService;
import com.coreline.financetracker.orchestration.service.PipelineContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(1)
public class RawImportStep implements PipelineStep {

    private final RawImportService rawImportService;
    private final String sampleFile;

    public RawImportStep(
            RawImportService rawImportService,
            @Value("${pipeline.sample-file:sample-import.csv}") String sampleFile
    ) {
        this.rawImportService = rawImportService;
        this.sampleFile = sampleFile;
    }

    @Override
    public void execute(PipelineContext context) {
        try {
            ClassPathResource resource = new ClassPathResource(sampleFile);
            if (!resource.exists()) {
                throw new IllegalStateException(
                        "Sample import file not found: " + sampleFile
                );
            }

            byte[] data;
            try (InputStream inputStream = resource.getInputStream()) {
                data = inputStream.readAllBytes();
            }

            context.setBankName(AppConstants.DEFAULT_BANK);
            context.setOriginalFilename(sampleFile);
            context.setImportedFile(
                    rawImportService.importFile(
                            AppConstants.DEFAULT_BANK,
                            sampleFile,
                            data
                    )
            );

        } catch (Exception e) {
            throw new IllegalStateException("Failed to import sample file", e);
        }
    }
}
