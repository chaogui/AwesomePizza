package com.demo.pizzeria.repository;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCreatedTime(LocalDate date);

    List<Order> findByUserId(Long userId);
}
