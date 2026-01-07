package com.trading.common.enums;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    PENDING("待提交"),
    PLACED("已提交"),
    PARTIAL_FILLED("部分成交"),
    FILLED("完全成交"),
    CANCELLED("已撤销"),
    REJECTED("已拒绝");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}