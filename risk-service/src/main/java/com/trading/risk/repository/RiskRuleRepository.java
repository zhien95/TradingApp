package com.trading.risk.repository;

import com.trading.common.enums.RiskType;
import com.trading.risk.entity.RiskRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskRuleRepository extends JpaRepository<RiskRule, Long> {
    List<RiskRule> findByRiskType(RiskType riskType);

    List<RiskRule> findByIsActiveTrue();
}