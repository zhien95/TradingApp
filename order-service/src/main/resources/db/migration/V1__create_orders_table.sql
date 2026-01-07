CREATE TABLE orders
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id           VARCHAR(50) UNIQUE NOT NULL,
    user_id            VARCHAR(50)        NOT NULL,
    symbol             VARCHAR(20)        NOT NULL,
    order_type         ENUM('MARKET', 'LIMIT', 'STOP', 'STOP_LIMIT') NOT NULL,
    side               ENUM('BUY', 'SELL') NOT NULL,
    quantity           DECIMAL(15, 8)     NOT NULL,
    price              DECIMAL(15, 8),
    stop_price         DECIMAL(15, 8),
    status             ENUM('PENDING', 'PLACED', 'PARTIAL_FILLED', 'FILLED', 'CANCELLED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    filled_quantity    DECIMAL(15, 8)              DEFAULT 0.00000000,
    average_fill_price DECIMAL(15, 8)              DEFAULT 0.00000000,
    created_at         TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX              idx_order_id (order_id),
    INDEX              idx_user_id (user_id),
    INDEX              idx_symbol (symbol),
    INDEX              idx_status (status),
    INDEX              idx_created_at (created_at)
);