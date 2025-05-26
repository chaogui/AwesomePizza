package com.demo.pizzeria.request;

import com.demo.pizzeria.data.Topping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class CreatePizzaRequest {

    String name;
    BigDecimal price;
    String description;
    Set<String> toppingNames;
}
