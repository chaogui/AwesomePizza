package com.demo.pizzeria.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    String name;
    //List<Topping> toppings;
    BigDecimal price;
    String description;
}
