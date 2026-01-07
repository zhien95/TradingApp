package com.trading.risk.dto;

import com.trading.common.enums.RiskSeverity;
import lombok.Data;

@Data
public class RiskCheckResponse {
    private boolean approved;
    private String message;
    private RiskSeverity severity;
    private String ruleViolated;
}