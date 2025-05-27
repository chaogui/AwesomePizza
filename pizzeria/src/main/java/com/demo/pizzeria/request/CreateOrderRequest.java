package com.demo.pizzeria.request;

import com.demo.pizzeria.dto.OrderItemDto;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    List<OrderItemDto> items;
}
