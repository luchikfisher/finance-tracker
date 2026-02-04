package com.coreline.financetracker.export.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GoogleSheetsClient {

    private final Sheets sheets;
    private final String spreadsheetId;

    public GoogleSheetsClient(
            Sheets sheets,
            // spreadsheet id is configured externally
            @org.springframework.beans.factory.annotation.Value("${google.sheets.spreadsheet-id}")
            String spreadsheetId
    ) {
        this.sheets = sheets;
        this.spreadsheetId = spreadsheetId;
    }

    public void clearAndWrite(
            String sheetName,
            List<List<Object>> values
    ) {
        try {
            sheets.spreadsheets().values()
                    .clear(spreadsheetId, sheetName, new ClearValuesRequest())
                    .execute();

            ValueRange body = new ValueRange().setValues(values);

            sheets.spreadsheets().values()
                    .update(spreadsheetId, sheetName, body)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to write to Google Sheets", e);
        }
    }
}
