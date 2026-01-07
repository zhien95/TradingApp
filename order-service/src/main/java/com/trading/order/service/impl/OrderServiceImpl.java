package com.trading.order.service.impl;

import com.trading.common.avro.OrderUpdate;
import com.trading.common.enums.OrderStatus;
import com.trading.common.enums.OrderType;
import com.trading.order.dto.NewOrderRequest;
import com.trading.order.dto.OrderDTO;
import com.trading.order.dto.OrderUpdateRequest;
import com.trading.order.entity.Order;
import com.trading.order.repository.OrderRepository;
import com.trading.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public OrderDTO createOrder(NewOrderRequest request) {
        log.info("Creating new order for user: {}, symbol: {}", request.getUserId(), request.getSymbol());

        // 验证订单参数
        if (!validateOrder(request)) {
            throw new IllegalArgumentException("Invalid order parameters");
        }

        // 创建订单实体
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setSymbol(request.getSymbol());
        order.setOrderType(request.getOrderType());
        order.setSide(request.getSide());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setStopPrice(request.getStopPrice());
        order.setStatus(OrderStatus.PENDING); // 初始状态为待处理
        order.setFilledQuantity(BigDecimal.ZERO);
        order.setAverageFillPrice(BigDecimal.ZERO);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getOrderId());

        // 发送订单创建事件到Kafka
        sendOrderUpdateToKafka(savedOrder, OrderStatus.PENDING);

        return convertToDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDTO> getOrderById(String orderId) {
        log.debug("Fetching order with ID: {}", orderId);
        return orderRepository.findByOrderId(orderId)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUser(String userId) {
        log.debug("Fetching orders for user: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserAndStatus(String userId, OrderStatus status) {
        log.debug("Fetching orders for user: {} with status: {}", userId, status);
        List<Order> orders = orderRepository.findByUserIdAndStatus(userId, status);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersBySymbol(String symbol) {
        log.debug("Fetching orders for symbol: {}", symbol);
        List<Order> orders = orderRepository.findBySymbol(symbol);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(OrderUpdateRequest request) {
        log.info("Updating order status for order ID: {}, new status: {}",
                request.getOrderId(), request.getStatus());

        Order order = orderRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + request.getOrderId()));

        // 更新订单状态和其他相关字段
        order.setStatus(request.getStatus());
        if (request.getFilledQuantity() != null) {
            order.setFilledQuantity(request.getFilledQuantity());
        }
        if (request.getAverageFillPrice() != null) {
            order.setAverageFillPrice(request.getAverageFillPrice());
        }

        Order updatedOrder = orderRepository.save(order);

        // 发送订单更新事件到Kafka
        sendOrderUpdateToKafka(updatedOrder, request.getStatus());

        return convertToDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(String orderId) {
        log.info("Cancelling order with ID: {}", orderId);

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.FILLED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel order that is already filled or cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        // 发送订单取消事件到Kafka
        sendOrderUpdateToKafka(cancelledOrder, OrderStatus.CANCELLED);

        return convertToDTO(cancelledOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateOrder(NewOrderRequest request) {
        log.debug("Validating order for user: {}, symbol: {}", request.getUserId(), request.getSymbol());

        // 检查必要字段
        if (request.getUserId() == null || request.getSymbol() == null ||
                request.getQuantity() == null || request.getSide() == null) {
            log.warn("Order validation failed: Missing required fields");
            return false;
        }

        // 检查数量是否为正
        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Order validation failed: Quantity must be positive");
            return false;
        }

        // 对于限价单，检查价格是否为正
        if (request.getOrderType() == OrderType.LIMIT &&
                (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            log.warn("Order validation failed: Limit orders must have positive price");
            return false;
        }

        // 对于止损单，检查止损价格是否为正
        if ((request.getOrderType() == OrderType.STOP || request.getOrderType() == OrderType.STOP_LIMIT) &&
                (request.getStopPrice() == null || request.getStopPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            log.warn("Order validation failed: Stop orders must have positive stop price");
            return false;
        }

        return true;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setSymbol(order.getSymbol());
        dto.setOrderType(order.getOrderType());
        dto.setSide(order.getSide());
        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        dto.setStopPrice(order.getStopPrice());
        dto.setStatus(order.getStatus());
        dto.setFilledQuantity(order.getFilledQuantity());
        dto.setAverageFillPrice(order.getAverageFillPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    private void sendOrderUpdateToKafka(Order order, OrderStatus newStatus) {
        try {
            OrderUpdate orderUpdate = OrderUpdate.newBuilder()
                    .setSeqId(System.currentTimeMillis())
                    .setOrderId(order.getOrderId())
                    .setUserId(order.getUserId())
                    .setSymbol(order.getSymbol())
                    .setOrderType(com.trading.common.avro.OrderType.valueOf(order.getOrderType().name()))
                    .setSide(com.trading.common.avro.OrderSide.valueOf(order.getSide().name()))
                    .setQuantity(order.getQuantity().doubleValue())
                    .setPrice(order.getPrice() != null ? order.getPrice().doubleValue() : 0.0)
                    .setStatus(com.trading.common.avro.OrderStatus.valueOf(newStatus.name()))
                    .setTimestamp(order.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .setFilledQuantity(order.getFilledQuantity() != null ? order.getFilledQuantity().doubleValue() : 0.0)
                    .setAverageFillPrice(order.getAverageFillPrice() != null ? order.getAverageFillPrice().doubleValue() : 0.0)
                    .build();

            kafkaTemplate.send("order-updates", order.getOrderId(), orderUpdate);
            log.debug("Sent order update to Kafka for order: {}, status: {}", order.getOrderId(), newStatus);
        } catch (Exception e) {
            log.error("Error sending order update to Kafka for order: {}", order.getOrderId(), e);
        }
    }
}