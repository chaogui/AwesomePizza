package com.demo.pizzeria.repository;

import com.demo.pizzeria.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCreatedTime(LocalDate date);

    List<Order> findByUserId(Long userId);

    @Query(value = "SELECT * FROM orders WHERE status = 'CREATED' ORDER BY created_time", nativeQuery = true)
    List<Order> findOrdersToWork();
}
