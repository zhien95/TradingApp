package com.trading.market.service;

import com.trading.market.dto.MarketDataDTO;
import com.trading.market.dto.MarketSymbolDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

public interface MarketDataService {
    MarketDataDTO getCurrentPrice(String symbol);

    List<MarketDataDTO> getHistoricalData(String symbol, int limit);

    List<MarketSymbolDTO> getAllSymbols();

    Optional<MarketSymbolDTO> getSymbol(String symbol);

    MarketDataDTO updateMarketData(MarketDataDTO marketDataDTO);

    void addSymbol(MarketSymbolDTO symbolDTO);

    boolean symbolExists(String symbol);

    SseEmitter streamMarketData(String symbol);

    SseEmitter streamAllMarketData();
}