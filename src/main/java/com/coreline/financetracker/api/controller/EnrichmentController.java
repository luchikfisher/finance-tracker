package com.coreline.financetracker.api.controller;

import com.coreline.financetracker.api.dto.EnrichmentDto;
import com.coreline.financetracker.enrichment.store.EnrichmentStore;
import com.coreline.financetracker.user.service.CurrentUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrichments")
public class EnrichmentController {

    private final EnrichmentStore enrichmentStore;
    private final CurrentUserService currentUserService;

    public EnrichmentController(
            EnrichmentStore enrichmentStore,
            CurrentUserService currentUserService
    ) {
        this.enrichmentStore = enrichmentStore;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    @PreAuthorize("@access.canRead(authentication)")
    public List<EnrichmentDto> listEnrichments() {
        return enrichmentStore.allByUserId(currentUserService.requireUserId()).stream()
                .map(EnrichmentDto::from)
                .toList();
    }
}
