package com.trading.order.dto;

import com.trading.common.enums.OrderSide;
import com.trading.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewOrderRequest {
    private String userId;
    private String symbol;
    private OrderType orderType;
    private OrderSide side;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal stopPrice;
}