package com.trading.risk.dto;

import com.trading.common.enums.OrderSide;
import com.trading.common.enums.RiskType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskCheckRequest {
    private String userId;
    private String symbol;
    private OrderSide side;
    private BigDecimal quantity;
    private BigDecimal price;
    private RiskType riskType;
}