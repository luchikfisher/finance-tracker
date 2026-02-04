package com.coreline.financetracker.orchestration.service;

import com.coreline.financetracker.parsing.service.ParsingService;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ParsingStep implements PipelineStep {

    private final ParsingService parsingService;

    public ParsingStep(ParsingService parsingService) {
        this.parsingService = parsingService;
    }

    @Override
    public void execute() {
        InputStream inputStream = InputStream.nullInputStream();

        parsingService.parse(
                "DUMMY_BANK",
                inputStream
        );
    }
}
