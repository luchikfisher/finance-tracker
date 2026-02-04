package com.coreline.financetracker.importraw.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "imported_files", uniqueConstraints = {
                @UniqueConstraint(name = "uq_imported_files_checksum", columnNames = "checksum")
        }
)
public class ImportedFile {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID importSessionId;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false, length = 64)
    private String checksum;

    @Column(nullable = false)
    private Instant uploadedAt;

    protected ImportedFile() {
        // JPA
    }

    public ImportedFile(
            UUID id,
            UUID importSessionId,
            String originalFilename,
            String bankName,
            String checksum,
            Instant uploadedAt
    ) {
        this.id = id;
        this.importSessionId = importSessionId;
        this.originalFilename = originalFilename;
        this.bankName = bankName;
        this.checksum = checksum;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getImportSessionId() {
        return importSessionId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getBankName() {
        return bankName;
    }

    public String getChecksum() {
        return checksum;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }
}
