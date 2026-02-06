package com.coreline.financetracker.parsing.impl;

import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.parsing.api.BankFileParser;
import com.coreline.financetracker.parsing.model.*;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DummyCsvBankParser implements BankFileParser {

    @Override
    public String supportedBank() {
        return AppConstants.DEFAULT_BANK;
    }

    @Override
    public List<ParsedTransaction> parse(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<ParsedTransaction> result = new ArrayList<>();

            // Expected CSV:
            // date,valueDate,amount,currency,direction,description,counterparty,accountId
            String line;
            boolean firstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 8) {
                    throw new IllegalArgumentException(
                            "Invalid CSV line " + lineNumber + ": expected 8 columns, got " + parts.length
                    );
                }

                ParsedTransaction tx = new ParsedTransaction(
                        new ParsedAccountRef(
                                AppConstants.DEFAULT_BANK,
                                parts[7].trim()
                        ),
                        LocalDate.parse(parts[0].trim()),
                        parseValueDate(parts[1], parts[0]),
                        new BigDecimal(parts[2].trim()),
                        parts[3].trim(),
                        ParsedDirection.valueOf(parts[4].trim().toUpperCase()),
                        parts[5].trim(),
                        parts[6].trim()
                );

                result.add(tx);
            }

            return result;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private LocalDate parseValueDate(String valueDate, String transactionDate) {
        String trimmed = valueDate == null ? "" : valueDate.trim();
        if (trimmed.isEmpty()) {
            return LocalDate.parse(transactionDate.trim());
        }
        return LocalDate.parse(trimmed);
    }
}
