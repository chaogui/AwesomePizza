package com.demo.pizzeria.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Entity
@Table(name = "Toppings")
public class Topping {

    @Id
    Long id;
    String name;
}
