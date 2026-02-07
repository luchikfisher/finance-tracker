package com.coreline.financetracker.enrichment.repository;

import com.coreline.financetracker.enrichment.model.TransactionEnrichment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionEnrichmentRepository extends JpaRepository<TransactionEnrichment, UUID> {

    List<TransactionEnrichment> findByUserId(UUID userId);
}
