package com.coreline.financetracker.importraw.service;

import com.coreline.financetracker.common.exception.ValidationException;
import com.coreline.financetracker.common.util.Preconditions;
import com.coreline.financetracker.common.time.ClockProvider;
import com.coreline.financetracker.importraw.model.*;
import com.coreline.financetracker.importraw.repository.*;
import com.coreline.financetracker.importraw.storage.RawFileStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class RawImportService {

    private final ImportSessionRepository importSessionRepository;
    private final ImportedFileRepository importedFileRepository;
    private final ChecksumService checksumService;
    private final RawFileStorage rawFileStorage;
    private final ClockProvider clockProvider;

    public RawImportService(
            ImportSessionRepository importSessionRepository,
            ImportedFileRepository importedFileRepository,
            ChecksumService checksumService,
            RawFileStorage rawFileStorage,
            ClockProvider clockProvider
    ) {
        this.importSessionRepository = importSessionRepository;
        this.importedFileRepository = importedFileRepository;
        this.checksumService = checksumService;
        this.rawFileStorage = rawFileStorage;
        this.clockProvider = clockProvider;
    }

    @Transactional
    public ImportedFile importFile(
            UUID userId,
            String bankName,
            String originalFilename,
            InputStream inputStream
    ) {
        Preconditions.notNull(userId, "userId must not be null");
        try {
            byte[] data = inputStream.readAllBytes();
            return importFile(userId, bankName, originalFilename, data);
        } catch (Exception e) {
            throw new ValidationException("Failed to read import file", e);
        }
    }

    @Transactional
    public ImportedFile importFile(
            UUID userId,
            String bankName,
            String originalFilename,
            byte[] data
    ) {
        Preconditions.notNull(userId, "userId must not be null");
        UUID sessionId = UUID.randomUUID();
        ImportSession session = new ImportSession(
                sessionId,
                userId,
                clockProvider.now(),
                ImportStatus.CREATED
        );
        importSessionRepository.save(session);

        String checksum = checksumService.sha256(
                new ByteArrayInputStream(data)
        );

        importedFileRepository.findByUserIdAndChecksum(userId, checksum)
                .ifPresent(existing -> {
                    throw new ValidationException(
                            "File with same checksum already imported: " + existing.getId()
                    );
        });

        UUID fileId = UUID.randomUUID();
        rawFileStorage.store(fileId, new ByteArrayInputStream(data));

        ImportedFile importedFile = new ImportedFile(
                fileId,
                sessionId,
                userId,
                originalFilename,
                bankName,
                checksum,
                clockProvider.now()
        );

        importedFileRepository.save(importedFile);
        session.markStored();

        return importedFile;
    }
}
