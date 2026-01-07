package com.trading.market.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "market_symbols")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketSymbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String exchange;

    @Column(length = 10)
    private String currency;

    @Column(length = 20)
    private String type; // STOCK, FOREX, CRYPTO, FUTURE, etc.

    @Column(name = "is_active")
    private Boolean isActive = true;
}