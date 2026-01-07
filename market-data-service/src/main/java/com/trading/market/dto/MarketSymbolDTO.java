package com.trading.market.dto;

import lombok.Data;

@Data
public class MarketSymbolDTO {
    private String symbol;
    private String name;
    private String exchange;
    private String currency;
    private String type; // STOCK, FOREX, CRYPTO, FUTURE, etc.
}