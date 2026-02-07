-- ============================
-- USERS AND OWNERSHIP (H2)
-- ============================

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

ALTER TABLE import_sessions
    ADD COLUMN user_id UUID;

ALTER TABLE imported_files
    ADD COLUMN user_id UUID;

ALTER TABLE accounts
    ADD COLUMN user_id UUID;

ALTER TABLE transactions
    ADD COLUMN user_id UUID;

ALTER TABLE transaction_enrichments
    ADD COLUMN user_id UUID;

ALTER TABLE import_sessions
    ADD CONSTRAINT fk_import_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users (id);

ALTER TABLE imported_files
    ADD CONSTRAINT fk_imported_files_user
        FOREIGN KEY (user_id)
        REFERENCES users (id);


ALTER TABLE accounts
    ADD CONSTRAINT fk_accounts_user
        FOREIGN KEY (user_id)
        REFERENCES users (id);

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id)
        REFERENCES users (id);

ALTER TABLE transaction_enrichments
    ADD CONSTRAINT fk_enrichments_user
        FOREIGN KEY (user_id)
        REFERENCES users (id);

CREATE INDEX idx_import_sessions_user
    ON import_sessions (user_id);

CREATE INDEX idx_imported_files_user
    ON imported_files (user_id);

CREATE INDEX idx_accounts_user
    ON accounts (user_id);

ALTER TABLE accounts
    DROP CONSTRAINT IF EXISTS uq_accounts_bank_external;

ALTER TABLE accounts
    ADD CONSTRAINT uq_accounts_user_bank_external
        UNIQUE (user_id, bank_name, external_account_id);

CREATE INDEX idx_transactions_user
    ON transactions (user_id);

CREATE INDEX idx_enrichments_user
    ON transaction_enrichments (user_id);
