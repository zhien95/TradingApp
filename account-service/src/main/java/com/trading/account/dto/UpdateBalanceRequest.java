package com.trading.account.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceRequest {
    private String accountNumber;
    private BigDecimal amount;
    private String transactionType; // DEBIT or CREDIT
}