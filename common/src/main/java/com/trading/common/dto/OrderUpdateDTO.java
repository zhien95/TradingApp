package com.trading.common.dto;

import com.trading.common.enums.OrderSide;
import com.trading.common.enums.OrderStatus;
import com.trading.common.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Map;

/**
 * 订单更新DTO类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {
    @NotNull(message = "序列号不能为空")
    private Long seqId;
    
    @NotNull(message = "订单ID不能为空")
    private String orderId;
    
    @NotNull(message = "用户ID不能为空")
    private String userId;
    
    @NotNull(message = "交易标的不能为空")
    private String symbol;
    
    private OrderType orderType;
    
    private OrderSide side;
    
    @Positive(message = "数量必须为正数")
    private Double quantity;
    
    @Positive(message = "价格必须为正数")
    private Double price;
    
    private OrderStatus status;
    
    private Long timestamp;
    
    @Positive(message = "已成交数量必须为正数")
    private Double filledQuantity;
    
    private Double averageFillPrice;
    
    private Map<String, String> metadata;
}