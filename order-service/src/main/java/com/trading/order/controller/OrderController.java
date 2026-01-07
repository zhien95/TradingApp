package com.trading.order.controller;

import com.trading.common.enums.OrderStatus;
import com.trading.order.dto.NewOrderRequest;
import com.trading.order.dto.OrderDTO;
import com.trading.order.dto.OrderUpdateRequest;
import com.trading.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody NewOrderRequest request) {
        log.info("Received request to create order: {}", request);
        OrderDTO order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        log.debug("Received request to get order: {}", orderId);
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String userId) {
        log.debug("Received request to get orders for user: {}", userId);
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserAndStatus(
            @PathVariable String userId,
            @PathVariable OrderStatus status) {
        log.debug("Received request to get orders for user: {} with status: {}", userId, status);
        List<OrderDTO> orders = orderService.getOrdersByUserAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<OrderDTO>> getOrdersBySymbol(@PathVariable String symbol) {
        log.debug("Received request to get orders for symbol: {}", symbol);
        List<OrderDTO> orders = orderService.getOrdersBySymbol(symbol);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@RequestBody OrderUpdateRequest request) {
        log.info("Received request to update order status: {}", request);
        OrderDTO order = orderService.updateOrderStatus(request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable String orderId) {
        log.info("Received request to cancel order: {}", orderId);
        OrderDTO order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateOrder(@RequestBody NewOrderRequest request) {
        log.debug("Received request to validate order: {}", request);
        boolean isValid = orderService.validateOrder(request);
        return ResponseEntity.ok(isValid);
    }
}