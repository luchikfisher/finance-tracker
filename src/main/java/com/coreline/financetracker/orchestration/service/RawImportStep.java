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
    private final String systemUserId;

    public RawImportStep(
            RawImportService rawImportService,
            @Value("${pipeline.sample-file:sample-import.csv}") String sampleFile,
            @Value("${pipeline.system-user-id:}") String systemUserId
    ) {
        this.rawImportService = rawImportService;
        this.sampleFile = sampleFile;
        this.systemUserId = systemUserId == null ? "" : systemUserId.trim();
    }

    @Override
    public void execute(PipelineContext context) {
        if (systemUserId.isBlank()) {
            throw new IllegalStateException("pipeline.system-user-id is required for pipeline execution");
        }
        context.setUserId(java.util.UUID.fromString(systemUserId));

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
                            context.getUserId(),
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
