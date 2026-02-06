package com.coreline.financetracker.api.dto;

import com.coreline.financetracker.importraw.model.ImportedFile;

import java.time.Instant;
import java.util.UUID;

public record ImportedFileDto(
        UUID id,
        UUID importSessionId,
        String originalFilename,
        String bankName,
        String checksum,
        Instant uploadedAt
) {
    public static ImportedFileDto from(ImportedFile file) {
        return new ImportedFileDto(
                file.getId(),
                file.getImportSessionId(),
                file.getOriginalFilename(),
                file.getBankName(),
                file.getChecksum(),
                file.getUploadedAt()
        );
    }
}
