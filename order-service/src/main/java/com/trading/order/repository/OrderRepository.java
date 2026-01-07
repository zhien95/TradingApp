package com.trading.order.repository;

import com.trading.common.enums.OrderStatus;
import com.trading.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);

    List<Order> findByUserId(String userId);

    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);

    List<Order> findBySymbol(String symbol);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status IN :statuses")
    List<Order> findByUserIdWithStatusIn(@Param("userId") String userId,
                                         @Param("statuses") List<OrderStatus> statuses);
}