package com.trading.market.repository;

import com.trading.market.entity.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    List<MarketData> findBySymbol(String symbol);

    @Query("SELECT m FROM MarketData m WHERE m.symbol = :symbol ORDER BY m.timestamp DESC")
    List<MarketData> findLatestBySymbol(@Param("symbol") String symbol);
}