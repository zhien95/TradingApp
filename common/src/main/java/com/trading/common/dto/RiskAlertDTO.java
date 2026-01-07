package com.trading.common.dto;

import com.trading.common.enums.RiskSeverity;
import com.trading.common.enums.RiskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Map;

/**
 * 风险警报DTO类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAlertDTO {
    @NotNull(message = "序列号不能为空")
    private Long seqId;
    
    @NotNull(message = "警报ID不能为空")
    private String alertId;
    
    @NotNull(message = "用户ID不能为空")
    private String userId;
    
    private RiskType riskType;
    
    private RiskSeverity severity;
    
    @Positive(message = "风险评分必须为正数")
    private Double score;
    
    private Long timestamp;
    
    private String description;
    
    private Map<String, String> metadata;
}