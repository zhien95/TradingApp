package com.trading.market.service.impl;

import com.trading.common.avro.PriceUpdate;
import com.trading.market.dto.MarketDataDTO;
import com.trading.market.dto.MarketSymbolDTO;
import com.trading.market.entity.MarketData;
import com.trading.market.entity.MarketSymbol;
import com.trading.market.repository.MarketDataRepository;
import com.trading.market.repository.MarketSymbolRepository;
import com.trading.market.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketDataServiceImpl implements MarketDataService {

    private final MarketDataRepository marketDataRepository;
    private final MarketSymbolRepository marketSymbolRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional(readOnly = true)
    public MarketDataDTO getCurrentPrice(String symbol) {
        log.debug("Fetching current price for symbol: {}", symbol);
        List<MarketData> marketDataList = marketDataRepository.findLatestBySymbol(symbol);
        if (!marketDataList.isEmpty()) {
            MarketData latestData = marketDataList.get(0);
            return convertToDTO(latestData);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketDataDTO> getHistoricalData(String symbol, int limit) {
        log.debug("Fetching historical data for symbol: {}, limit: {}", symbol, limit);
        List<MarketData> marketDataList = marketDataRepository.findBySymbol(symbol);
        return marketDataList.stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketSymbolDTO> getAllSymbols() {
        log.debug("Fetching all market symbols");
        List<MarketSymbol> symbols = marketSymbolRepository.findAll();
        return symbols.stream()
                .map(this::convertToSymbolDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarketSymbolDTO> getSymbol(String symbol) {
        log.debug("Fetching symbol: {}", symbol);
        return marketSymbolRepository.findBySymbol(symbol)
                .map(this::convertToSymbolDTO);
    }

    @Override
    @Transactional
    public MarketDataDTO updateMarketData(MarketDataDTO marketDataDTO) {
        log.info("Updating market data for symbol: {}", marketDataDTO.getSymbol());

        // 验证符号是否存在
        if (!symbolExists(marketDataDTO.getSymbol())) {
            throw new IllegalArgumentException("Symbol does not exist: " + marketDataDTO.getSymbol());
        }

        MarketData marketData = convertToEntity(marketDataDTO);
        MarketData savedData = marketDataRepository.save(marketData);

        // 发送价格更新到Kafka
        sendPriceUpdateToKafka(savedData);

        return convertToDTO(savedData);
    }

    @Override
    @Transactional
    public void addSymbol(MarketSymbolDTO symbolDTO) {
        log.info("Adding new symbol: {}", symbolDTO.getSymbol());

        MarketSymbol symbol = new MarketSymbol();
        symbol.setSymbol(symbolDTO.getSymbol());
        symbol.setName(symbolDTO.getName());
        symbol.setExchange(symbolDTO.getExchange());
        symbol.setCurrency(symbolDTO.getCurrency());
        symbol.setType(symbolDTO.getType());
        symbol.setIsActive(true);

        marketSymbolRepository.save(symbol);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean symbolExists(String symbol) {
        return marketSymbolRepository.findBySymbolAndIsActiveTrue(symbol).isPresent();
    }

    private MarketDataDTO convertToDTO(MarketData entity) {
        MarketDataDTO dto = new MarketDataDTO();
        dto.setSymbol(entity.getSymbol());
        dto.setPrice(entity.getPrice());
        dto.setBidPrice(entity.getBidPrice());
        dto.setAskPrice(entity.getAskPrice());
        dto.setHighPrice(entity.getHighPrice());
        dto.setLowPrice(entity.getLowPrice());
        dto.setOpenPrice(entity.getOpenPrice());
        dto.setClosePrice(entity.getClosePrice());
        dto.setVolume(entity.getVolume());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }

    private MarketData convertToEntity(MarketDataDTO dto) {
        MarketData entity = new MarketData();
        entity.setSymbol(dto.getSymbol());
        entity.setPrice(dto.getPrice());
        entity.setBidPrice(dto.getBidPrice());
        entity.setAskPrice(dto.getAskPrice());
        entity.setHighPrice(dto.getHighPrice());
        entity.setLowPrice(dto.getLowPrice());
        entity.setOpenPrice(dto.getOpenPrice());
        entity.setClosePrice(dto.getClosePrice());
        entity.setVolume(dto.getVolume());
        entity.setTimestamp(dto.getTimestamp());
        return entity;
    }

    private MarketSymbolDTO convertToSymbolDTO(MarketSymbol entity) {
        MarketSymbolDTO dto = new MarketSymbolDTO();
        dto.setSymbol(entity.getSymbol());
        dto.setName(entity.getName());
        dto.setExchange(entity.getExchange());
        dto.setCurrency(entity.getCurrency());
        dto.setType(entity.getType());
        return dto;
    }

    private void sendPriceUpdateToKafka(MarketData marketData) {
        try {
            PriceUpdate priceUpdate = PriceUpdate.newBuilder()
                    .setSeqId(System.currentTimeMillis())
                    .setSymbol(marketData.getSymbol())
                    .setPrice(marketData.getPrice() != null ? marketData.getPrice().doubleValue() : 0.0)
                    .setTimestamp(marketData.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .setSource("MARKET_DATA_SERVICE")
                    .setBidPrice(marketData.getBidPrice() != null ? marketData.getBidPrice().doubleValue() : 0.0)
                    .setAskPrice(marketData.getAskPrice() != null ? marketData.getAskPrice().doubleValue() : 0.0)
                    .setVolume(marketData.getVolume() != null ? marketData.getVolume().doubleValue() : 0.0)
                    .build();

            kafkaTemplate.send("price-updates", marketData.getSymbol(), priceUpdate);
            log.debug("Sent price update to Kafka for symbol: {}", marketData.getSymbol());
        } catch (Exception e) {
            log.error("Error sending price update to Kafka", e);
        }
    }
}