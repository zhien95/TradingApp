package com.trading.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class Account {
    private String userId;
    private Map<String, BigDecimal> balances; // 资金余额
    private Map<String, BigDecimal> positions; // 持仓
    private BigDecimal totalAssets;
}