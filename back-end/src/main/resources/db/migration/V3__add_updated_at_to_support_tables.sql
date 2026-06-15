ALTER TABLE financial_transactions
    ADD COLUMN updated_at TIMESTAMP;

ALTER TABLE auth_tokens
    ADD COLUMN updated_at TIMESTAMP;

DROP INDEX IF EXISTS idx_messages_client_status;

ALTER TABLE messages
    DROP COLUMN client_id;
