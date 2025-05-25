package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.repository.OrderRepository;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.request.UpdateOrderRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    @Override
    public Order getOrderById(Long id) throws ResourceNotFoundException {
        return orderRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Order with id = " + id +" not found."));
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date)  {
        return orderRepository
                .findByDate(date);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository
                .findByUserId(userId);
    }

    @Override
    public Order createOrder(Order order) throws ResourceAlreadyExistsException {
        if(orderRepository.findById(order.getId()).isPresent()){
            throw new ResourceAlreadyExistsException("Order with id = "+order.getId() +" already exists. You may update the order.");
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, UpdateOrderRequest request) throws ResourceNotFoundException {
        return orderRepository
                .findById(id)
                .map(orderInDb -> updateExistingOrder(orderInDb, request))
                .map(orderRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("Order with id = " + id +" not found."));
    }

    @Override
    public void deleteOrderById(Long id) throws ResourceNotFoundException {
        if(getOrderById(id) == null){
            throw new ResourceNotFoundException("Pizza with id = " + id +" not found.");
        }
        orderRepository.deleteById(id);
    }


    public Order updateExistingOrder(Order order, UpdateOrderRequest request) {
//        order.setName(request.getName());
//        order.setToppings(request.getToppings());
//        order.setPrice(request.getPrice());
        return order;
    }

}
