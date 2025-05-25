package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.request.UpdateOrderRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;

import java.time.LocalDate;
import java.util.List;

public interface IOrderService {
    Order getOrderById(Long id) throws ResourceNotFoundException;
    List<Order> getOrders();
    List<Order> getOrdersByDate(LocalDate date);
    List<Order> getOrdersByUserId(Long userId);
    Order createOrder(Order Order) throws ResourceAlreadyExistsException;
    Order updateOrder(Long id, UpdateOrderRequest request) throws ResourceNotFoundException;
    void deleteOrderById(Long id) throws ResourceNotFoundException;
}
