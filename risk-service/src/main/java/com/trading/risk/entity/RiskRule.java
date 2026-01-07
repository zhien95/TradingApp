package com.trading.risk.entity;

import com.trading.common.enums.RiskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "risk_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskType riskType;

    @Column(length = 500)
    private String description;

    @Column(name = "max_exposure", precision = 15, scale = 4)
    private BigDecimal maxExposure;

    @Column(name = "max_daily_loss", precision = 15, scale = 4)
    private BigDecimal maxDailyLoss;

    @Column(name = "max_position_size", precision = 15, scale = 8)
    private BigDecimal maxPositionSize;

    @Column(name = "min_balance", precision = 15, scale = 4)
    private BigDecimal minBalance;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}