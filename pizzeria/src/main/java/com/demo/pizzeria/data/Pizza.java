package com.demo.pizzeria.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "Pizze")
public class Pizza {

    Long id;
    String name;
    List<Topping> toppings;
    BigDecimal price;
}
