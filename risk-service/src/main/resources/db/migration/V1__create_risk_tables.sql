CREATE TABLE risk_rules
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name         VARCHAR(100) UNIQUE NOT NULL,
    risk_type         ENUM('CREDIT', 'MARKET', 'LIQUIDITY', 'OPERATIONAL', 'COMPLIANCE') NOT NULL,
    description       VARCHAR(500),
    max_exposure      DECIMAL(15, 4),
    max_daily_loss    DECIMAL(15, 4),
    max_position_size DECIMAL(15, 8),
    min_balance       DECIMAL(15, 4),
    is_active         BOOLEAN                      DEFAULT TRUE,
    created_at        TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX             idx_rule_name (rule_name),
    INDEX             idx_risk_type (risk_type),
    INDEX             idx_is_active (is_active)
);

CREATE TABLE risk_limits
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       VARCHAR(50)    NOT NULL,
    risk_type     ENUM('CREDIT', 'MARKET', 'LIQUIDITY', 'OPERATIONAL', 'COMPLIANCE') NOT NULL,
    limit_value   DECIMAL(15, 4) NOT NULL,
    current_usage DECIMAL(15, 4) NOT NULL DEFAULT 0.0000,
    is_active     BOOLEAN                 DEFAULT TRUE,
    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX         idx_user_id (user_id),
    INDEX         idx_risk_type (risk_type),
    INDEX         idx_is_active (is_active),
    UNIQUE KEY uk_user_risk_type (user_id, risk_type)
);