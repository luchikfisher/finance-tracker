package com.coreline.financetracker.importraw.repository;

import com.coreline.financetracker.importraw.model.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ImportedFileRepository extends JpaRepository<ImportedFile, UUID> {

    Optional<ImportedFile> findByUserIdAndChecksum(UUID userId, String checksum);

    Page<ImportedFile> findByUserId(UUID userId, Pageable pageable);
}
