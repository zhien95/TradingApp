package com.trading.market.controller;

import com.trading.market.dto.MarketDataDTO;
import com.trading.market.dto.MarketSymbolDTO;
import com.trading.market.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market-data")
@RequiredArgsConstructor
@Slf4j
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<MarketDataDTO> getCurrentPrice(@PathVariable String symbol) {
        log.debug("Received request to get current price for symbol: {}", symbol);
        MarketDataDTO marketData = marketDataService.getCurrentPrice(symbol);
        return marketData != null ? ResponseEntity.ok(marketData) : ResponseEntity.notFound().build();
    }

    @GetMapping("/historical/{symbol}")
    public ResponseEntity<List<MarketDataDTO>> getHistoricalData(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("Received request to get historical data for symbol: {}, limit: {}", symbol, limit);
        List<MarketDataDTO> marketDataList = marketDataService.getHistoricalData(symbol, limit);
        return ResponseEntity.ok(marketDataList);
    }

    @GetMapping("/symbols")
    public ResponseEntity<List<MarketSymbolDTO>> getAllSymbols() {
        log.debug("Received request to get all symbols");
        List<MarketSymbolDTO> symbols = marketDataService.getAllSymbols();
        return ResponseEntity.ok(symbols);
    }

    @GetMapping("/symbols/{symbol}")
    public ResponseEntity<MarketSymbolDTO> getSymbol(@PathVariable String symbol) {
        log.debug("Received request to get symbol: {}", symbol);
        return marketDataService.getSymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/symbols")
    public ResponseEntity<Void> addSymbol(@RequestBody MarketSymbolDTO symbolDTO) {
        log.info("Received request to add symbol: {}", symbolDTO.getSymbol());
        marketDataService.addSymbol(symbolDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<MarketDataDTO> updateMarketData(@RequestBody MarketDataDTO marketDataDTO) {
        log.info("Received request to update market data for symbol: {}", marketDataDTO.getSymbol());
        MarketDataDTO updatedData = marketDataService.updateMarketData(marketDataDTO);
        return ResponseEntity.ok(updatedData);
    }

    @GetMapping("/exists/{symbol}")
    public ResponseEntity<Boolean> symbolExists(@PathVariable String symbol) {
        log.debug("Received request to check if symbol exists: {}", symbol);
        boolean exists = marketDataService.symbolExists(symbol);
        return ResponseEntity.ok(exists);
    }
}