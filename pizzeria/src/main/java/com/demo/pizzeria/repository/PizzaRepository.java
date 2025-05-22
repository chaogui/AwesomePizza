package com.demo.pizzeria.repository;

import com.demo.pizzeria.data.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findByName(String name);
    Boolean existsByName(String name);
}
