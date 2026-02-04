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

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                ParsedTransaction tx = new ParsedTransaction(
                        new ParsedAccountRef(AppConstants.DEFAULT_BANK, parts[7]),
                        LocalDate.parse(parts[0]),
                        LocalDate.parse(parts[1]),
                        new BigDecimal(parts[2]),
                        parts[3],
                        ParsedDirection.valueOf(parts[4]),
                        parts[5],
                        parts[6]
                );

                result.add(tx);
            }

            return result;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }
}
