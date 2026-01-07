package com.trading.common.enums;

/**
 * 订单方向枚举
 */
public enum OrderSide {
    BUY("买入"),
    SELL("卖出");

    private final String description;

    OrderSide(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}