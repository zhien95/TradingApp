package com.trading.risk.service;

import com.trading.common.enums.RiskType;
import com.trading.risk.dto.RiskCheckRequest;
import com.trading.risk.dto.RiskCheckResponse;
import com.trading.risk.dto.RiskLimitDTO;
import com.trading.risk.dto.RiskRuleDTO;

import java.util.List;

public interface RiskService {
    RiskCheckResponse checkRisk(RiskCheckRequest request);

    RiskRuleDTO createRiskRule(RiskRuleDTO riskRuleDTO);

    List<RiskRuleDTO> getAllRiskRules();

    List<RiskRuleDTO> getRiskRulesByType(RiskType riskType);

    RiskLimitDTO createRiskLimit(RiskLimitDTO riskLimitDTO);

    RiskLimitDTO updateRiskLimit(String userId, RiskType riskType, Double newLimit);

    List<RiskLimitDTO> getUserLimits(String userId);

    boolean isWithinRiskLimits(RiskCheckRequest request);

    void updateRiskMetrics(String userId, double transactionAmount);
}