package com.coreline.financetracker.config;

import com.coreline.financetracker.common.constants.AppConstants;
import com.coreline.financetracker.common.exception.ExternalIntegrationException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class GoogleConfig {
    @Value("${google.credentials.path}")
    String credentialsPath;

    private static final List<String> SHEETS_SCOPES =
            List.of(AppConstants.GOOGLE_SHEETS_SCOPE);

    @Bean
    public Sheets googleSheets(
            @Value("${google.application-name}") String applicationName
    ) {
        try {
            InputStream credentialsStream =
                    getClass().getResourceAsStream(credentialsPath);

            if (credentialsStream == null) {
                throw new IllegalStateException(
                        "google-service-account.json not found in classpath");
            }

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(credentialsStream)
                    .createScoped(SHEETS_SCOPES);

            HttpRequestInitializer requestInitializer =
                    new HttpCredentialsAdapter(credentials);

            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    requestInitializer
            )
                    .setApplicationName(applicationName)
                    .build();

        } catch (Exception e) {
            throw new ExternalIntegrationException(
                    "Failed to initialize Google Sheets client", e);
        }
    }
}