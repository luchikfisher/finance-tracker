package com.coreline.financetracker.parsing.api;

import com.coreline.financetracker.parsing.model.ParsedTransaction;

import java.io.InputStream;
import java.util.List;

public interface BankFileParser {

    /**
     * @return bank identifier this parser supports
     */
    String supportedBank();

    /**
     * Parse raw bank file into intermediate transactions
     */
    List<ParsedTransaction> parse(InputStream inputStream);
}