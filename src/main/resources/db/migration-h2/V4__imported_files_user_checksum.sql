-- ============================
-- IMPORTED FILES UNIQUE CHECKSUM PER USER (H2)
-- ============================

ALTER TABLE imported_files
    DROP CONSTRAINT IF EXISTS uq_imported_files_checksum;

ALTER TABLE imported_files
    ADD CONSTRAINT uq_imported_files_user_checksum
        UNIQUE (user_id, checksum);
