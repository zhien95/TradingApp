package com.trading.common.dto;

import com.trading.common.enums.TransactionStatus;
import com.trading.common.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 交易DTO类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    @NotNull(message = "序列号不能为空")
    private Long seqId;
    
    @NotNull(message = "交易ID不能为空")
    private String transactionId;
    
    @NotNull(message = "用户ID不能为空")
    private String userId;
    
    @Positive(message = "交易金额必须为正数")
    private Double amount;
    
    private String currency;
    
    private TransactionType transactionType;
    
    private TransactionStatus status;
    
    private Long timestamp;
    
    private Map<String, String> metadata;
}