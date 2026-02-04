package com.coreline.financetracker.export.service;

import com.coreline.financetracker.export.api.Exporter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

    private final List<Exporter> exporters;

    public ExportService(List<Exporter> exporters) {
        this.exporters = exporters;
    }

    public void exportAll() {
        exporters.forEach(Exporter::export);
    }
}
