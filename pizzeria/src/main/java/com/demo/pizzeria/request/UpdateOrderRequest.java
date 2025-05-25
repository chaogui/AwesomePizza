package com.demo.pizzeria.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateOrderRequest {
    String orderStatus;
    LocalDateTime completionDate;
}
