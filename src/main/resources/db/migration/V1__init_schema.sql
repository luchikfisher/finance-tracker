-- ============================
-- FINANCE TRACKER
-- Initial Database Schema
-- ============================

-- ============================
-- IMPORT RAW LAYER
-- ============================

CREATE TABLE import_sessions (
    id UUID PRIMARY KEY,
    started_at TIMESTAMP NOT NULL,
    status VARCHAR(32) NOT NULL
);

CREATE TABLE imported_files (
    id UUID PRIMARY KEY,
    import_session_id UUID NOT NULL,
    original_filename TEXT NOT NULL,
    bank_name TEXT NOT NULL,
    checksum VARCHAR(64) NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_imported_files_session
        FOREIGN KEY (import_session_id)
        REFERENCES import_sessions (id),

    CONSTRAINT uq_imported_files_checksum
        UNIQUE (checksum)
);

-- ============================
-- DOMAIN: ACCOUNTS
-- ============================

CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    bank_name TEXT NOT NULL,
    external_account_id TEXT NOT NULL,

    CONSTRAINT uq_accounts_bank_external
        UNIQUE (bank_name, external_account_id)
);

-- ============================
-- DOMAIN: TRANSACTIONS
-- ============================

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    transaction_date DATE NOT NULL,
    value_date DATE NOT NULL,

    amount NUMERIC(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,

    direction VARCHAR(8) NOT NULL,
    description TEXT NOT NULL,
    counterparty TEXT,

    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id)
        REFERENCES accounts (id)
);

CREATE INDEX idx_transactions_account_date
    ON transactions (account_id, transaction_date);

-- ============================
-- ENRICHMENT
-- ============================

CREATE TABLE transaction_enrichments (
    transaction_id UUID PRIMARY KEY,
    category VARCHAR(64) NOT NULL,
    source VARCHAR(32) NOT NULL,
    explanation TEXT NOT NULL,
    enriched_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_enrichment_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES transactions (id)
);

-- ============================
-- (FUTURE) DEDUPLICATION TRACE
-- ============================

-- Optional: store fingerprints later if desired
-- CREATE TABLE transaction_fingerprints (
--     transaction_id UUID PRIMARY KEY,
--     fingerprint VARCHAR(64) NOT NULL UNIQUE
-- );

-- ============================
-- END OF SCHEMA
-- ============================
