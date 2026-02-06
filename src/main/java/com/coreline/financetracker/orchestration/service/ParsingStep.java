package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.importraw.storage.RawFileStorage;
import com.coreline.financetracker.parsing.service.ParsingService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(2)
public class ParsingStep implements PipelineStep {

    private final ParsingService parsingService;
    private final RawFileStorage rawFileStorage;

    public ParsingStep(
            ParsingService parsingService,
            RawFileStorage rawFileStorage
    ) {
        this.parsingService = parsingService;
        this.rawFileStorage = rawFileStorage;
    }

    @Override
    public void execute(PipelineContext context) {
        if (context.getImportedFile() == null) {
            throw new IllegalStateException("No imported file available for parsing");
        }

        try (InputStream inputStream =
                     rawFileStorage.load(context.getImportedFile().getId())) {
            context.setParsedTransactions(
                    parsingService.parse(
                            context.getImportedFile().getBankName(),
                            inputStream
                    )
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse imported file", e);
        }
    }
}
