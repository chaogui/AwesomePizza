package com.demo.pizzeria.repository;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.data.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ToppingRepository extends JpaRepository<Topping, Long> {
    Optional<Topping> findByName(String name);
    Boolean existsByName(String name);
}
