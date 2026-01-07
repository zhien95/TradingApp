package com.trading.account.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    private String ownerName;
    private BigDecimal initialBalance;
    private String currency;
}