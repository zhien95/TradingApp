package com.trading.common.enums;

/**
 * 交易类型枚举
 */
public enum TransactionType {
    DEBIT("借记"),
    CREDIT("贷记"),
    TRANSFER("转账"),
    PAYMENT("支付"),
    WITHDRAWAL("提现"),
    DEPOSIT("存款");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}