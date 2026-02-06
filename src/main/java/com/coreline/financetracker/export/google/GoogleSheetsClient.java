package com.coreline.financetracker.export.google;

import com.coreline.financetracker.common.exception.ExternalIntegrationException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty(name = "export.google.enabled", havingValue = "true")
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
            ensureSheetExists(sheetName);

            sheets.spreadsheets().values()
                    .clear(spreadsheetId, sheetName, new ClearValuesRequest())
                    .execute();

            ValueRange body = new ValueRange().setValues(values);

            sheets.spreadsheets().values()
                    .update(spreadsheetId, sheetName, body)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (IOException e) {
            throw new ExternalIntegrationException(
                    "Failed to write to Google Sheets",
                    e
            );
        }
    }

    private void ensureSheetExists(String sheetName) throws IOException {
        Spreadsheet spreadsheet = sheets.spreadsheets()
                .get(spreadsheetId)
                .setFields("sheets.properties.title")
                .execute();

        boolean exists = spreadsheet.getSheets().stream()
                .map(Sheet::getProperties)
                .map(SheetProperties::getTitle)
                .anyMatch(sheetName::equals);

        if (exists) {
            return;
        }

        AddSheetRequest addSheetRequest = new AddSheetRequest()
                .setProperties(new SheetProperties().setTitle(sheetName));

        BatchUpdateSpreadsheetRequest request = new BatchUpdateSpreadsheetRequest()
                .setRequests(List.of(new Request().setAddSheet(addSheetRequest)));

        sheets.spreadsheets().batchUpdate(spreadsheetId, request).execute();
    }
}
