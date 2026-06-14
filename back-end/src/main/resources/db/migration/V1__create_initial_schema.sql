CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(120) NOT NULL,
    document_id VARCHAR(20) NOT NULL UNIQUE,
    document_type VARCHAR(4) NOT NULL CHECK (document_type IN ('CPF', 'CNPJ')),
    plan_type VARCHAR(10) NOT NULL CHECK (plan_type IN ('PREPAID', 'POSTPAID')),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    balance NUMERIC(10, 2),
    monthly_limit NUMERIC(10, 2),
    monthly_used NUMERIC(10, 2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE recipients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL REFERENCES clients(id),
    recipient_id UUID NOT NULL REFERENCES recipients(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_conversations_client_recipient UNIQUE (client_id, recipient_id)
);

CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL REFERENCES conversations(id),
    client_id UUID NOT NULL REFERENCES clients(id),
    content TEXT NOT NULL,
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('NORMAL', 'URGENT')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('QUEUED', 'PROCESSING', 'SENT', 'DELIVERED', 'READ', 'FAILED')),
    sent_by_id UUID NOT NULL,
    sent_by_type VARCHAR(10) NOT NULL CHECK (sent_by_type IN ('CLIENT', 'USER')),
    cost NUMERIC(10, 2) NOT NULL,
    queued_at TIMESTAMP NOT NULL,
    processing_at TIMESTAMP,
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    read_at TIMESTAMP,
    failed_at TIMESTAMP,
    failure_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE financial_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL REFERENCES clients(id),
    message_id UUID REFERENCES messages(id),
    type VARCHAR(20) NOT NULL CHECK (type IN ('CREDIT', 'DEBIT', 'LIMIT_USAGE', 'LIMIT_ADJUSTMENT')),
    amount NUMERIC(10, 2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE auth_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL REFERENCES clients(id),
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_conversations_client_id ON conversations(client_id);
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_client_status ON messages(client_id, status);
CREATE INDEX idx_messages_priority_queued_at ON messages(priority, queued_at);
CREATE INDEX idx_financial_transactions_client_id ON financial_transactions(client_id);
CREATE INDEX idx_auth_tokens_token ON auth_tokens(token);
