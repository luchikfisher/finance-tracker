package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.domain.model.Transaction;
import com.coreline.financetracker.enrichment.model.EnrichmentResult;
import com.coreline.financetracker.importraw.model.ImportedFile;
import com.coreline.financetracker.parsing.model.ParsedTransaction;

import java.util.ArrayList;
import java.util.List;

public class PipelineContext {

    private java.util.UUID userId;
    private String bankName;
    private String originalFilename;
    private ImportedFile importedFile;
    private List<ParsedTransaction> parsedTransactions = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<EnrichmentResult> enrichmentResults = new ArrayList<>();

    public java.util.UUID getUserId() {
        return userId;
    }

    public void setUserId(java.util.UUID userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public ImportedFile getImportedFile() {
        return importedFile;
    }

    public void setImportedFile(ImportedFile importedFile) {
        this.importedFile = importedFile;
    }

    public List<ParsedTransaction> getParsedTransactions() {
        return parsedTransactions;
    }

    public void setParsedTransactions(List<ParsedTransaction> parsedTransactions) {
        this.parsedTransactions = parsedTransactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<EnrichmentResult> getEnrichmentResults() {
        return enrichmentResults;
    }

    public void setEnrichmentResults(List<EnrichmentResult> enrichmentResults) {
        this.enrichmentResults = enrichmentResults;
    }
}
