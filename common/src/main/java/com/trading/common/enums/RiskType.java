package com.trading.common.enums;

/**
 * 风险类型枚举
 */
public enum RiskType {
    CREDIT("信用风险"),
    MARKET("市场风险"),
    LIQUIDITY("流动性风险"),
    OPERATIONAL("操作风险"),
    COMPLIANCE("合规风险");

    private final String description;

    RiskType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}