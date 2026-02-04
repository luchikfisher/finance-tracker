package com.coreline.financetracker.importraw.repository;

import com.coreline.financetracker.importraw.model.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImportedFileRepository extends JpaRepository<ImportedFile, UUID> {

    Optional<ImportedFile> findByChecksum(String checksum);
}