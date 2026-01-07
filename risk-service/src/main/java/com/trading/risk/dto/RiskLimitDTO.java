package com.trading.risk.dto;

import com.trading.common.enums.RiskType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskLimitDTO {
    private Long id;
    private String userId;
    private RiskType riskType;
    private BigDecimal limitValue;
    private BigDecimal currentUsage;
    private Boolean isActive;
}