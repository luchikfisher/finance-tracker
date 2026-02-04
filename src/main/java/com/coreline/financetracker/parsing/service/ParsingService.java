package com.coreline.financetracker.parsing.service;

import com.coreline.financetracker.parsing.api.BankFileParser;
import com.coreline.financetracker.parsing.model.ParsedTransaction;
import com.coreline.financetracker.parsing.registry.ParserRegistry;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ParsingService {

    private final ParserRegistry parserRegistry;

    public ParsingService(ParserRegistry parserRegistry) {
        this.parserRegistry = parserRegistry;
    }

    public List<ParsedTransaction> parse(
            String bankName,
            InputStream inputStream
    ) {
        BankFileParser parser = parserRegistry.getParser(bankName);
        return parser.parse(inputStream);
    }
}
