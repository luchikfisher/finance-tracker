package com.coreline.financetracker.importraw.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "import_sessions")
public class ImportSession {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Instant startedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportStatus status;

    protected ImportSession() {
        // JPA
    }

    public ImportSession(UUID id, Instant startedAt, ImportStatus status) {
        this.id = id;
        this.startedAt = startedAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public void markStored() {
        this.status = ImportStatus.STORED;
    }

    public void markFailed() {
        this.status = ImportStatus.FAILED;
    }
}
