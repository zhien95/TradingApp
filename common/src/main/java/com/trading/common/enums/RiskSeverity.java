package com.trading.common.enums;

/**
 * 风险严重程度枚举
 */
public enum RiskSeverity {
    LOW("低"),
    MEDIUM("中"),
    HIGH("高"),
    CRITICAL("严重");

    private final String description;

    RiskSeverity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}