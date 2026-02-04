package com.coreline.financetracker.parsing.registry;

import com.coreline.financetracker.parsing.api.BankFileParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ParserRegistry {

    private final Map<String, BankFileParser> parsersByBank;

    public ParserRegistry(List<BankFileParser> parsers) {
        this.parsersByBank = parsers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        BankFileParser::supportedBank,
                        Function.identity()
                ));
    }

    public BankFileParser getParser(String bankName) {
        BankFileParser parser = parsersByBank.get(bankName);
        if (parser == null) {
            throw new IllegalArgumentException(
                    "No parser registered for bank: " + bankName
            );
        }
        return parser;
    }
}
