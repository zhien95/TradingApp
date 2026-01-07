package com.trading.order.service;

import com.trading.common.enums.OrderStatus;
import com.trading.order.dto.NewOrderRequest;
import com.trading.order.dto.OrderDTO;
import com.trading.order.dto.OrderUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDTO createOrder(NewOrderRequest request);

    Optional<OrderDTO> getOrderById(String orderId);

    List<OrderDTO> getOrdersByUser(String userId);

    List<OrderDTO> getOrdersByUserAndStatus(String userId, OrderStatus status);

    List<OrderDTO> getOrdersBySymbol(String symbol);

    OrderDTO updateOrderStatus(OrderUpdateRequest request);

    OrderDTO cancelOrder(String orderId);

    boolean validateOrder(NewOrderRequest request);
}