package com.trading.common.enums;

/**
 * 订单类型枚举
 */
public enum OrderType {
    MARKET("市价单"),
    LIMIT("限价单"),
    STOP("止损单"),
    STOP_LIMIT("止损限价单");

    private final String description;

    OrderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}