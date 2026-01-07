CREATE TABLE market_data
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol      VARCHAR(20) NOT NULL,
    price       DECIMAL(15, 4),
    bid_price   DECIMAL(15, 4),
    ask_price   DECIMAL(15, 4),
    high_price  DECIMAL(15, 4),
    low_price   DECIMAL(15, 4),
    open_price  DECIMAL(15, 4),
    close_price DECIMAL(15, 4),
    volume      DECIMAL(15, 4),
    timestamp   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX       idx_symbol (symbol),
    INDEX       idx_timestamp (timestamp)
);

CREATE TABLE market_symbols
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol    VARCHAR(20) UNIQUE NOT NULL,
    name      VARCHAR(100)       NOT NULL,
    exchange  VARCHAR(50),
    currency  VARCHAR(10),
    type      VARCHAR(20), -- STOCK, FOREX, CRYPTO, FUTURE, etc.
    is_active BOOLEAN DEFAULT TRUE,
    INDEX     idx_symbol (symbol),
    INDEX     idx_is_active (is_active)
);