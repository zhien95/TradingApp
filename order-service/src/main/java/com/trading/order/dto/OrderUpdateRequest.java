package com.trading.order.dto;

import com.trading.common.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderUpdateRequest {
    private String orderId;
    private OrderStatus status;
    private BigDecimal filledQuantity;
    private BigDecimal averageFillPrice;
}