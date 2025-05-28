package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.dto.OrderItemDto;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.repository.OrderRepository;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.request.CreateOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;


    @BeforeEach
    public void setup() {

        order = new Order();
        order.setId(1L);
        order.setStatus("CREATED");

    }

    @Test
    @org.junit.jupiter.api.Order(1)//test OK
    public void createOrderTest() throws ResourceAlreadyExistsException {

        Pizza pizza1 = new Pizza();
        pizza1.setId(1L);
        pizza1.setName("Margherita");
        pizza1.setPrice(BigDecimal.valueOf(10.00));

        // precondition
        given(pizzaRepository.findById(pizza1.getId())).willReturn(Optional.of(pizza1));
        when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setPizzaId(pizza1.getId());
        orderItemDto.setPizzaName(pizza1.getName());
        orderItemDto.setPrice(pizza1.getPrice());
        orderItemDto.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(orderItemDto));

        //action
        Order createdOrder = orderService.createOrder(request);

        // verify the output
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getTotalPrice()).isEqualTo(pizza1.getPrice().multiply(BigDecimal.valueOf(2)));
    }

    @Test
    @org.junit.jupiter.api.Order(2)//test OK
    public void getOrderById() throws ResourceNotFoundException {
        // precondition
        given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(order));

        // action
        Order existingOrder = orderService.getOrderById(order.getId());

        // verify
        assertThat(existingOrder).isNotNull();
        assertThat(existingOrder.getId()).isEqualTo(order.getId());
        assertThat(existingOrder.getStatus()).isEqualTo(order.getStatus());

    }


    @Test
    @org.junit.jupiter.api.Order(3) //test OK
    public void getAllOrders() {

        Order order1 = new Order();
        order1.setId(2L);
        order1.setStatus("CREATED");
        order1.setTotalPrice(BigDecimal.valueOf(30.00));

        // precondition
        given(orderRepository.findAll()).willReturn(List.of(order, order1));

        // action
        List<Order> orderList = orderService.getOrders();

        // verify
        assertThat(orderList).isNotNull();
        assertThat(orderList.size()).isEqualTo(2);
    }

    @Test
    @org.junit.jupiter.api.Order(4)//test OK
    public void updateOrderStatus() throws ResourceNotFoundException {

        // precondition
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        given(orderRepository.save(order)).willReturn(order);

        // action
        orderService.updateStatus(order.getId(), "COMPLETED");
        Order updatedorder = orderService.getOrderById(order.getId());

        // verify
        assertThat(updatedorder.getStatus()).isEqualTo("COMPLETED");

    }

    @Test
    @org.junit.jupiter.api.Order(5)//test OK
    public void deleteOrder() throws ResourceNotFoundException {

        // precondition
        willDoNothing().given(orderRepository).deleteById(order.getId());
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        // action
        orderService.deleteOrderById(order.getId());

        // verify
        verify(orderRepository, times(1)).deleteById(order.getId());
    }

}

