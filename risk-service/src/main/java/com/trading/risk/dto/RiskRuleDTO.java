package com.trading.risk.dto;

import com.trading.common.enums.RiskType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskRuleDTO {
    private Long id;
    private String ruleName;
    private RiskType riskType;
    private String description;
    private BigDecimal maxExposure;
    private BigDecimal maxDailyLoss;
    private BigDecimal maxPositionSize;
    private BigDecimal minBalance;
    private Boolean isActive;
}