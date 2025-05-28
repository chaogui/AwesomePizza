package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.request.CreateOrderRequest;
import com.demo.pizzeria.request.UpdateOrderRequest;
import com.demo.pizzeria.service.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IOrderService orderService;


    Order order;

    @BeforeEach
    public void setup() {

        order = new Order();
        order.setId(1L);
        order.setStatus("CREATED");
        order.setTotalPrice(BigDecimal.valueOf(30.0));
//        order.setName("Margherita");
//        order.setDescription("Margherita Desc");
//        order.setPrice(BigDecimal.valueOf(10.00));
        //order.setToppings();
    }

    //Post Controller
    @Test
    @org.junit.jupiter.api.Order(1)
    public void saveOrderTest() throws Exception, ResourceAlreadyExistsException {
        // precondition
        given(orderService.createOrder(any(CreateOrderRequest.class))).willReturn(order);

        // action
        ResultActions response = mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        // verify
        response.andDo(print())
                .andExpect(jsonPath("$.data.status", is(order.getStatus())))
                .andExpect(jsonPath("$.data.id", is(order.getId().intValue())));
    }

    //Get Controller
    @Test
    @org.junit.jupiter.api.Order(2)//test OK
    public void getOrdersTest() throws Exception {
        // precondition
        List<Order> list = new ArrayList<>();
        list.add(order);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus("CREATED");
        order2.setTotalPrice(BigDecimal.valueOf(10.00));
        list.add(order2);

        given(orderService.getOrders()).willReturn(list);

        // action
        ResultActions response = mockMvc.perform(get("/order"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.size()", is(list.size())));

    }

    //get by Id controller
    @Test
    @org.junit.jupiter.api.Order(3)//test OK
    public void getOrderByIdTest() throws Exception {
        // precondition
        given(orderService.getOrderById(order.getId())).willReturn(order);

        // action
        ResultActions response = mockMvc.perform(get("/order/{id}", order.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.status", is(order.getStatus())))
                .andExpect(jsonPath("$.data.id", is(order.getId().intValue())));

    }


    //Update Order
    @Test
    @org.junit.jupiter.api.Order(4)//test OK
    public void updateOrderTest() throws Exception {
        // precondition
        given(orderService.getOrderById(order.getId())).willReturn(order);

        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setOrderStatus("COMPLETED");
        request.setCompletionDate(LocalDateTime.now());

        order.setStatus(request.getOrderStatus());
        order.setCompletedTime(request.getCompletionDate());

        given(orderService.updateOrder(order.getId(), request)).willReturn(order);

        // action
        ResultActions response = mockMvc
                .perform(put("/order/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.status", is(request.getOrderStatus())))
                .andExpect(jsonPath("$.data.id", is(order.getId())));
    }


    // delete Order
    @Test
    @org.junit.jupiter.api.Order(5)//test OK
    public void deleteOrderTest() throws Exception {
        // precondition
        willDoNothing().given(orderService).deleteOrderById(order.getId());

        // action
        ResultActions response = mockMvc.perform(delete("/order/{id}", order.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}