package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.OrderItem;
import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.dto.OrderDto;
import com.demo.pizzeria.dto.OrderItemDto;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.repository.OrderRepository;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.request.CreateOrderRequest;
import com.demo.pizzeria.request.UpdateOrderRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final PizzaRepository pizzaRepository;
    private final ModelMapper modelMapper;

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
                .findByCreatedTime(date);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository
                .findByUserId(userId);
    }

    @Override
    public Order createOrder(CreateOrderRequest request) throws ResourceAlreadyExistsException {
//        if(orderRepository.findById(order.getId()).isPresent()){
//            throw new ResourceAlreadyExistsException("Order with id = "+order.getId() +" already exists. You may update the order.");
//        }
        Order order = new Order();
        order.setCreatedTime(LocalDateTime.now());
        order.setStatus("CREATED");

        Set<OrderItem> set = new HashSet<>();
        Pizza pizza;
        OrderItem orderItem;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(OrderItemDto item: request.getItems()){
            pizza = pizzaRepository.findById(item.getPizzaId()).get();
            orderItem = new OrderItem(order, pizza, item.getQuantity(), item.getPrice());
            set.add(orderItem);
            totalPrice = totalPrice.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        order.setTotalPrice(totalPrice);
        order.setItems(set);

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
    public void updateStatus(Long id, String newStatus) throws ResourceNotFoundException {
        orderRepository
                .findById(id)
                .map(orderInDb -> updateStatus(orderInDb, newStatus))
                .map(orderRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("Order with id = " + id +" not found."));
    }

    private Order updateStatus(Order orderInDb, String newStatus) {
        orderInDb.setStatus(newStatus);
        return orderInDb;
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

    @Override
    public OrderDto convertToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        Set<OrderItemDto> setItems = order
                .getItems()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
        orderDto.setItems(setItems);
        return orderDto;
    }

    public OrderItemDto convertToDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setPizzaId(item.getPizza().getId());
        dto.setPizzaName(item.getPizza().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    public Order findOrderToWork() {
        List<Order> orders = orderRepository.findOrdersToWork();
        if(orders.isEmpty()){
            return null;
        }
        return orders.get(0);
    }
}
