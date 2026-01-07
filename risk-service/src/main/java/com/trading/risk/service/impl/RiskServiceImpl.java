package com.trading.risk.service.impl;

import com.trading.common.avro.RiskAlert;
import com.trading.common.enums.RiskSeverity;
import com.trading.common.enums.RiskType;
import com.trading.risk.dto.RiskCheckRequest;
import com.trading.risk.dto.RiskCheckResponse;
import com.trading.risk.dto.RiskLimitDTO;
import com.trading.risk.dto.RiskRuleDTO;
import com.trading.risk.entity.RiskLimit;
import com.trading.risk.entity.RiskRule;
import com.trading.risk.repository.RiskLimitRepository;
import com.trading.risk.repository.RiskRuleRepository;
import com.trading.risk.service.RiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskServiceImpl implements RiskService {

    private final RiskRuleRepository riskRuleRepository;
    private final RiskLimitRepository riskLimitRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional(readOnly = true)
    public RiskCheckResponse checkRisk(RiskCheckRequest request) {
        log.info("Performing risk check for user: {}, symbol: {}, amount: {}",
                request.getUserId(), request.getSymbol(), calculateOrderValue(request));

        // 检查是否在风险限制内
        boolean withinLimits = isWithinRiskLimits(request);

        RiskCheckResponse response = new RiskCheckResponse();
        response.setApproved(withinLimits);

        if (withinLimits) {
            response.setMessage("Risk check passed");
            response.setSeverity(RiskSeverity.LOW);
            log.info("Risk check passed for user: {}", request.getUserId());
        } else {
            response.setMessage("Risk check failed - violates risk limits");
            response.setSeverity(RiskSeverity.HIGH);
            response.setRuleViolated("Risk limit exceeded");
            log.warn("Risk check failed for user: {}, rule violated", request.getUserId());

            // 发送风险警报
            sendRiskAlert(request, "RISK_LIMIT_EXCEEDED", RiskSeverity.HIGH);
        }

        return response;
    }

    @Override
    @Transactional
    public RiskRuleDTO createRiskRule(RiskRuleDTO riskRuleDTO) {
        log.info("Creating risk rule: {}", riskRuleDTO.getRuleName());

        RiskRule riskRule = new RiskRule();
        riskRule.setRuleName(riskRuleDTO.getRuleName());
        riskRule.setRiskType(riskRuleDTO.getRiskType());
        riskRule.setDescription(riskRuleDTO.getDescription());
        riskRule.setMaxExposure(riskRuleDTO.getMaxExposure());
        riskRule.setMaxDailyLoss(riskRuleDTO.getMaxDailyLoss());
        riskRule.setMaxPositionSize(riskRuleDTO.getMaxPositionSize());
        riskRule.setMinBalance(riskRuleDTO.getMinBalance());
        riskRule.setIsActive(riskRuleDTO.getIsActive());

        RiskRule savedRule = riskRuleRepository.save(riskRule);
        log.info("Risk rule created successfully: {}", savedRule.getRuleName());

        return convertToRuleDTO(savedRule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RiskRuleDTO> getAllRiskRules() {
        log.debug("Fetching all risk rules");
        List<RiskRule> rules = riskRuleRepository.findAll();
        return rules.stream()
                .map(this::convertToRuleDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RiskRuleDTO> getRiskRulesByType(RiskType riskType) {
        log.debug("Fetching risk rules for type: {}", riskType);
        List<RiskRule> rules = riskRuleRepository.findByRiskType(riskType);
        return rules.stream()
                .map(this::convertToRuleDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RiskLimitDTO createRiskLimit(RiskLimitDTO riskLimitDTO) {
        log.info("Creating risk limit for user: {}, type: {}",
                riskLimitDTO.getUserId(), riskLimitDTO.getRiskType());

        RiskLimit riskLimit = new RiskLimit();
        riskLimit.setUserId(riskLimitDTO.getUserId());
        riskLimit.setRiskType(riskLimitDTO.getRiskType());
        riskLimit.setLimitValue(riskLimitDTO.getLimitValue());
        riskLimit.setCurrentUsage(BigDecimal.ZERO);
        riskLimit.setIsActive(riskLimitDTO.getIsActive());

        RiskLimit savedLimit = riskLimitRepository.save(riskLimit);
        log.info("Risk limit created successfully for user: {}", savedLimit.getUserId());

        return convertToLimitDTO(savedLimit);
    }

    @Override
    @Transactional
    public RiskLimitDTO updateRiskLimit(String userId, RiskType riskType, Double newLimit) {
        log.info("Updating risk limit for user: {}, type: {}, new limit: {}", userId, riskType, newLimit);

        RiskLimit riskLimit = riskLimitRepository.findByUserIdAndRiskType(userId, riskType)
                .orElseThrow(() -> new IllegalArgumentException("Risk limit not found for user: " + userId));

        riskLimit.setLimitValue(BigDecimal.valueOf(newLimit));
        RiskLimit updatedLimit = riskLimitRepository.save(riskLimit);

        return convertToLimitDTO(updatedLimit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RiskLimitDTO> getUserLimits(String userId) {
        log.debug("Fetching risk limits for user: {}", userId);
        List<RiskLimit> limits = riskLimitRepository.findByUserId(userId);
        return limits.stream()
                .map(this::convertToLimitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isWithinRiskLimits(RiskCheckRequest request) {
        log.debug("Checking if user {} is within risk limits", request.getUserId());

        // 计算订单价值
        BigDecimal orderValue = calculateOrderValue(request);

        // 获取用户的市场风险限制
        Optional<RiskLimit> marketRiskLimitOpt = riskLimitRepository.findByUserIdAndRiskType(
                request.getUserId(), RiskType.MARKET);

        // 如果存在市场风险限制，则检查是否超出
        if (marketRiskLimitOpt.isPresent()) {
            RiskLimit marketRiskLimit = marketRiskLimitOpt.get();
            if (marketRiskLimit.getIsActive() &&
                    marketRiskLimit.getCurrentUsage().add(orderValue).compareTo(marketRiskLimit.getLimitValue()) > 0) {
                log.warn("User {} would exceed market risk limit. Current: {}, Attempted addition: {}, Limit: {}",
                        request.getUserId(), marketRiskLimit.getCurrentUsage(), orderValue, marketRiskLimit.getLimitValue());
                return false;
            }
        }

        // 检查信用风险限制
        Optional<RiskLimit> creditRiskLimitOpt = riskLimitRepository.findByUserIdAndRiskType(
                request.getUserId(), RiskType.CREDIT);

        if (creditRiskLimitOpt.isPresent()) {
            RiskLimit creditRiskLimit = creditRiskLimitOpt.get();
            if (creditRiskLimit.getIsActive() &&
                    creditRiskLimit.getCurrentUsage().add(orderValue).compareTo(creditRiskLimit.getLimitValue()) > 0) {
                log.warn("User {} would exceed credit risk limit. Current: {}, Attempted addition: {}, Limit: {}",
                        request.getUserId(), creditRiskLimit.getCurrentUsage(), orderValue, creditRiskLimit.getLimitValue());
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional
    public void updateRiskMetrics(String userId, double transactionAmount) {
        log.info("Updating risk metrics for user: {}, amount: {}", userId, transactionAmount);

        // 更新市场风险指标
        updateRiskMetricForType(userId, RiskType.MARKET, transactionAmount);

        // 更新信用风险指标
        updateRiskMetricForType(userId, RiskType.CREDIT, transactionAmount);

        log.debug("Risk metrics updated successfully for user: {}", userId);
    }

    private void updateRiskMetricForType(String userId, RiskType riskType, double amount) {
        RiskLimit riskLimit = riskLimitRepository.findByUserIdAndRiskType(userId, riskType)
                .orElse(createDefaultRiskLimit(userId, riskType));

        if (riskLimit.getIsActive()) {
            riskLimit.setCurrentUsage(riskLimit.getCurrentUsage().add(BigDecimal.valueOf(amount)));
            riskLimitRepository.save(riskLimit);
        }
    }

    private RiskLimit createDefaultRiskLimit(String userId, RiskType riskType) {
        log.debug("Creating default risk limit for user: {}, type: {}", userId, riskType);

        RiskLimit defaultLimit = new RiskLimit();
        defaultLimit.setUserId(userId);
        defaultLimit.setRiskType(riskType);
        defaultLimit.setLimitValue(BigDecimal.valueOf(1000000)); // 默认100万美元的风险限制
        defaultLimit.setCurrentUsage(BigDecimal.ZERO);
        defaultLimit.setIsActive(true);

        return riskLimitRepository.save(defaultLimit);
    }

    private BigDecimal calculateOrderValue(RiskCheckRequest request) {
        if (request.getPrice() != null && request.getQuantity() != null) {
            return request.getPrice().multiply(request.getQuantity());
        }
        return BigDecimal.ZERO;
    }

    private RiskRuleDTO convertToRuleDTO(RiskRule rule) {
        RiskRuleDTO dto = new RiskRuleDTO();
        dto.setId(rule.getId());
        dto.setRuleName(rule.getRuleName());
        dto.setRiskType(rule.getRiskType());
        dto.setDescription(rule.getDescription());
        dto.setMaxExposure(rule.getMaxExposure());
        dto.setMaxDailyLoss(rule.getMaxDailyLoss());
        dto.setMaxPositionSize(rule.getMaxPositionSize());
        dto.setMinBalance(rule.getMinBalance());
        dto.setIsActive(rule.getIsActive());
        return dto;
    }

    private RiskLimitDTO convertToLimitDTO(RiskLimit limit) {
        RiskLimitDTO dto = new RiskLimitDTO();
        dto.setId(limit.getId());
        dto.setUserId(limit.getUserId());
        dto.setRiskType(limit.getRiskType());
        dto.setLimitValue(limit.getLimitValue());
        dto.setCurrentUsage(limit.getCurrentUsage());
        dto.setIsActive(limit.getIsActive());
        return dto;
    }

    private void sendRiskAlert(RiskCheckRequest request, String alertType, RiskSeverity severity) {
        try {
            RiskAlert riskAlert = RiskAlert.newBuilder()
                    .setSeqId(System.currentTimeMillis())
                    .setAlertId("ALERT_" + System.currentTimeMillis())
                    .setUserId(request.getUserId())
                    .setRiskType(com.trading.common.avro.RiskType.valueOf(request.getRiskType().name()))
                    .setSeverity(com.trading.common.avro.RiskSeverity.valueOf(severity.name()))
                    .setScore(severity == RiskSeverity.HIGH ? 90.0 : 50.0)
                    .setTimestamp(System.currentTimeMillis())
                    .setDescription("Risk violation: " + alertType + " for user " + request.getUserId())
                    .build();

            kafkaTemplate.send("risk-alerts", request.getUserId(), riskAlert);
            log.info("Sent risk alert to Kafka for user: {}, alert type: {}", request.getUserId(), alertType);
        } catch (Exception e) {
            log.error("Error sending risk alert to Kafka for user: {}", request.getUserId(), e);
        }
    }
}