package com.trading.order.dto;

import com.trading.common.enums.OrderSide;
import com.trading.common.enums.OrderStatus;
import com.trading.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private String orderId;
    private String userId;
    private String symbol;
    private OrderType orderType;
    private OrderSide side;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal stopPrice;
    private OrderStatus status;
    private BigDecimal filledQuantity;
    private BigDecimal averageFillPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}