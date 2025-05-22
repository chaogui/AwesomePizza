package com.demo.pizzeria.request;

import com.demo.pizzeria.data.Topping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdatePizzaRequest {

    String name;
    List<Topping> toppings;
    BigDecimal price;
}
