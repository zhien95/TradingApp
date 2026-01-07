package com.trading.common.enums;

/**
 * 交易状态枚举
 */
public enum TransactionStatus {
    PENDING("待处理"),
    COMPLETED("已完成"),
    FAILED("失败"),
    CANCELLED("已取消"),
    PROCESSING("处理中");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}