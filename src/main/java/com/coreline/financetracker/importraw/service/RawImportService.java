package com.coreline.financetracker.importraw.service;

import com.coreline.financetracker.importraw.model.*;
import com.coreline.financetracker.importraw.repository.*;
import com.coreline.financetracker.importraw.storage.RawFileStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Service
public class RawImportService {

    private final ImportSessionRepository importSessionRepository;
    private final ImportedFileRepository importedFileRepository;
    private final ChecksumService checksumService;
    private final RawFileStorage rawFileStorage;

    public RawImportService(
            ImportSessionRepository importSessionRepository,
            ImportedFileRepository importedFileRepository,
            ChecksumService checksumService,
            RawFileStorage rawFileStorage
    ) {
        this.importSessionRepository = importSessionRepository;
        this.importedFileRepository = importedFileRepository;
        this.checksumService = checksumService;
        this.rawFileStorage = rawFileStorage;
    }

    @Transactional
    public ImportedFile importFile(
            String bankName,
            String originalFilename,
            InputStream inputStream
    ) {
        UUID sessionId = UUID.randomUUID();
        ImportSession session = new ImportSession(
                sessionId,
                Instant.now(),
                ImportStatus.CREATED
        );
        importSessionRepository.save(session);

        String checksum = checksumService.sha256(inputStream);

        importedFileRepository.findByChecksum(checksum)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "File with same checksum already imported: " + existing.getId()
                    );
                });

        UUID fileId = UUID.randomUUID();
        rawFileStorage.store(fileId, inputStream);

        ImportedFile importedFile = new ImportedFile(
                fileId,
                sessionId,
                originalFilename,
                bankName,
                checksum,
                Instant.now()
        );

        importedFileRepository.save(importedFile);
        session.markStored();

        return importedFile;
    }
}
