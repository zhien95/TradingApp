package com.trading.market.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(precision = 15, scale = 4)
    private BigDecimal price;

    @Column(name = "bid_price", precision = 15, scale = 4)
    private BigDecimal bidPrice;

    @Column(name = "ask_price", precision = 15, scale = 4)
    private BigDecimal askPrice;

    @Column(name = "high_price", precision = 15, scale = 4)
    private BigDecimal highPrice;

    @Column(name = "low_price", precision = 15, scale = 4)
    private BigDecimal lowPrice;

    @Column(name = "open_price", precision = 15, scale = 4)
    private BigDecimal openPrice;

    @Column(name = "close_price", precision = 15, scale = 4)
    private BigDecimal closePrice;

    @Column(precision = 15, scale = 4)
    private BigDecimal volume;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        timestamp = LocalDateTime.now();
    }
}