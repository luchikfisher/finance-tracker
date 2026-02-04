package com.coreline.financetracker.common.constants;

public final class AppConstants {

    private AppConstants() {}

    // =====================
    // CHECKSUMS
    // =====================
    public static final String CHECKSUM_ALGORITHM = "SHA-256";

    // =====================
    // MONEY
    // =====================
    public static final int MONEY_SCALE = 2;

    // =====================
    // PIPELINE
    // =====================
    public static final String DEFAULT_BANK = "DUMMY_BANK";

    // =====================
    // GOOGLE SHEETS
    // =====================
    public static final String GOOGLE_SHEETS_VALUE_INPUT_OPTION = "RAW";


    public static final String GOOGLE_SHEETS_SCOPE =
            "https://www.googleapis.com/auth/spreadsheets";

}
