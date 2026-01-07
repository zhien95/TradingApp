package com.trading.account.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private String currency;
}