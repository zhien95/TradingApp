package com.trading.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private String orderId;
    private String userId;
    private String symbol;
    private String orderType; // LIMIT, MARKET
    private String orderSide; // BUY, SELL
    private BigDecimal price;
    private BigDecimal quantity;
    private String status; // PENDING, FILLED, PARTIAL_FILLED, CANCELLED
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}