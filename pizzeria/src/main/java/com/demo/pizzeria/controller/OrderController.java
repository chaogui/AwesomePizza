package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.request.UpdateOrderRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import com.demo.pizzeria.response.CustomResponse;
import com.demo.pizzeria.service.IOrderService;
import com.demo.pizzeria.service.IPizzaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/order")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping("/")
    public ResponseEntity<CustomResponse> getOrders(){
        log.info("getOrders called..");
        return ResponseEntity.ok(new CustomResponse("get success", orderService.getOrders()));
    }

    @GetMapping("/{date}")
    public ResponseEntity<CustomResponse> getOrdersByDate(@PathVariable("date") String date){
        log.info("getOrdersByDate called..");
        //string to LocalDate
        LocalDate localDate = LocalDate.parse(date); // formato date: yyyy-MM-dd
        return ResponseEntity.ok(new CustomResponse("get success", orderService.getOrdersByDate(localDate)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomResponse> getOrdersByUserId(@PathVariable("userId") Long userId){
        log.info("getOrdersByUserId called..");
        return ResponseEntity.ok(new CustomResponse("get success", orderService.getOrdersByUserId(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getOrderById(@PathVariable("id") Long id) {
        log.info("getOrderById called..");
        try {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(new CustomResponse("get success", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createOrder(Order order){
        log.info("createOrder called..");
        try {
            order = orderService.createOrder(order);
            return ResponseEntity.ok(new CustomResponse("create success", order));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updateOrderById(@PathVariable("id") Long id, UpdateOrderRequest request){
        log.info("updateOrderById called..");
        try {
            Order order = orderService.updateOrder(id, request);
            return ResponseEntity.ok(new CustomResponse("update success", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deleteOrderById(@PathVariable("id") Long id){
        log.info("deleteOrderById called..");
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok(new CustomResponse("delete success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse(e.getMessage(), null));
        }
    }
}
