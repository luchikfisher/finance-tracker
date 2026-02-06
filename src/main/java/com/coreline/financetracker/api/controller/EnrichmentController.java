package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.EnrichmentDto;
import com.coreline.financetracker.enrichment.store.EnrichmentStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrichments")
public class EnrichmentController {

    private final EnrichmentStore enrichmentStore;

    public EnrichmentController(EnrichmentStore enrichmentStore) {
        this.enrichmentStore = enrichmentStore;
    }

    @GetMapping
    public List<EnrichmentDto> listEnrichments() {
        return enrichmentStore.all().stream()
                .map(EnrichmentDto::from)
                .toList();
    }
}
