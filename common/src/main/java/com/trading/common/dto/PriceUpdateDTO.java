package com.trading.common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 价格更新DTO类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdateDTO {
    @NotNull(message = "序列号不能为空")
    private Long seqId;
    
    @NotNull(message = "交易标的不能为空")
    private String symbol;
    
    @Positive(message = "价格必须为正数")
    private Double price;
    
    private Long timestamp;
    
    private String source;
    
    @Positive(message = "买价必须为正数")
    private Double bidPrice;
    
    @Positive(message = "卖价必须为正数")
    private Double askPrice;
    
    @Positive(message = "成交量必须为正数")
    private Double volume;
    
    private Map<String, String> metadata;
}