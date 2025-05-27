package com.demo.pizzeria.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto {

    private Long id;
    private LocalDateTime createdTime;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private String status;
    private LocalDateTime completedTime;
    private Long userId;
    private Set<OrderItemDto> items = new HashSet<>();

}
