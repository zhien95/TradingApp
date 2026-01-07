package com.trading.risk.repository;

import com.trading.common.enums.RiskType;
import com.trading.risk.entity.RiskLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskLimitRepository extends JpaRepository<RiskLimit, Long> {
    List<RiskLimit> findByUserId(String userId);

    Optional<RiskLimit> findByUserIdAndRiskType(String userId, RiskType riskType);

    List<RiskLimit> findByUserIdAndIsActiveTrue(String userId);
}