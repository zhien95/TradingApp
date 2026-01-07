CREATE TABLE accounts
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number    VARCHAR(50) UNIQUE NOT NULL,
    owner_name        VARCHAR(255)       NOT NULL,
    balance           DECIMAL(19, 4)     NOT NULL DEFAULT 0.0000,
    available_balance DECIMAL(19, 4)     NOT NULL DEFAULT 0.0000,
    currency          VARCHAR(10)        NOT NULL DEFAULT 'USD',
    created_at        TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_account_number ON accounts (account_number);