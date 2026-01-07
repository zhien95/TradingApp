package com.trading.order.entity;

import com.trading.common.enums.OrderSide;
import com.trading.common.enums.OrderStatus;
import com.trading.common.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String orderId;

    @Column(nullable = false, length = 50)
    private String userId;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;

    @Column(nullable = false, precision = 15, scale = 8)
    private BigDecimal quantity;

    @Column(precision = 15, scale = 8)
    private BigDecimal price;

    @Column(precision = 15, scale = 8)
    private BigDecimal stopPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(precision = 15, scale = 8)
    private BigDecimal filledQuantity;

    @Column(precision = 15, scale = 8)
    private BigDecimal averageFillPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderId == null) {
            orderId = generateOrderId();
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
        if (filledQuantity == null) {
            filledQuantity = BigDecimal.ZERO;
        }
        if (averageFillPrice == null) {
            averageFillPrice = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis() + "-" + (int) (Math.random() * 10000);
    }
}