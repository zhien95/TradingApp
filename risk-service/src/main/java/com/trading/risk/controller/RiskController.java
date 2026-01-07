package com.trading.risk.controller;

import com.trading.common.enums.RiskType;
import com.trading.risk.dto.RiskCheckRequest;
import com.trading.risk.dto.RiskCheckResponse;
import com.trading.risk.dto.RiskLimitDTO;
import com.trading.risk.dto.RiskRuleDTO;
import com.trading.risk.service.RiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
@Slf4j
public class RiskController {

    private final RiskService riskService;

    @PostMapping("/check")
    public ResponseEntity<RiskCheckResponse> checkRisk(@RequestBody RiskCheckRequest request) {
        log.info("Received risk check request: {}", request);
        RiskCheckResponse response = riskService.checkRisk(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rules")
    public ResponseEntity<RiskRuleDTO> createRiskRule(@RequestBody RiskRuleDTO riskRuleDTO) {
        log.info("Received request to create risk rule: {}", riskRuleDTO.getRuleName());
        RiskRuleDTO rule = riskService.createRiskRule(riskRuleDTO);
        return ResponseEntity.ok(rule);
    }

    @GetMapping("/rules")
    public ResponseEntity<List<RiskRuleDTO>> getAllRiskRules() {
        log.debug("Received request to get all risk rules");
        List<RiskRuleDTO> rules = riskService.getAllRiskRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/rules/type/{riskType}")
    public ResponseEntity<List<RiskRuleDTO>> getRiskRulesByType(@PathVariable RiskType riskType) {
        log.debug("Received request to get risk rules for type: {}", riskType);
        List<RiskRuleDTO> rules = riskService.getRiskRulesByType(riskType);
        return ResponseEntity.ok(rules);
    }

    @PostMapping("/limits")
    public ResponseEntity<RiskLimitDTO> createRiskLimit(@RequestBody RiskLimitDTO riskLimitDTO) {
        log.info("Received request to create risk limit for user: {}", riskLimitDTO.getUserId());
        RiskLimitDTO limit = riskService.createRiskLimit(riskLimitDTO);
        return ResponseEntity.ok(limit);
    }

    @PutMapping("/limits/{userId}/{riskType}")
    public ResponseEntity<RiskLimitDTO> updateRiskLimit(
            @PathVariable String userId,
            @PathVariable RiskType riskType,
            @RequestParam Double newLimit) {
        log.info("Received request to update risk limit for user: {}, type: {}, new limit: {}",
                userId, riskType, newLimit);
        RiskLimitDTO limit = riskService.updateRiskLimit(userId, riskType, newLimit);
        return ResponseEntity.ok(limit);
    }

    @GetMapping("/limits/user/{userId}")
    public ResponseEntity<List<RiskLimitDTO>> getUserLimits(@PathVariable String userId) {
        log.debug("Received request to get risk limits for user: {}", userId);
        List<RiskLimitDTO> limits = riskService.getUserLimits(userId);
        return ResponseEntity.ok(limits);
    }

    @PostMapping("/metrics/update")
    public ResponseEntity<Void> updateRiskMetrics(
            @RequestParam String userId,
            @RequestParam Double transactionAmount) {
        log.info("Received request to update risk metrics for user: {}, amount: {}", userId, transactionAmount);
        riskService.updateRiskMetrics(userId, transactionAmount);
        return ResponseEntity.ok().build();
    }
}