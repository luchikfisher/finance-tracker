package com.coreline.financetracker.importraw.repository;

import com.coreline.financetracker.importraw.model.ImportSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImportSessionRepository extends JpaRepository<ImportSession, UUID> {

    List<ImportSession> findByUserId(UUID userId);
}
