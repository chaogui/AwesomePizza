package com.demo.pizzeria.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Long pizzaId;
    private String pizzaName;
    private BigDecimal price;
    private Integer quantity;

}
