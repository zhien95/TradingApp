package com.trading.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Trade {
    private String tradeId;
    private String buyOrderId;
    private String sellOrderId;
    private String symbol;
    private BigDecimal price;
    private BigDecimal quantity;
    private LocalDateTime tradeTime;
}