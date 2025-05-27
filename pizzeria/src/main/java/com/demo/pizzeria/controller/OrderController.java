package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.dto.OrderDto;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.request.CreateOrderRequest;
import com.demo.pizzeria.request.UpdateOrderRequest;
import com.demo.pizzeria.response.CustomResponse;
import com.demo.pizzeria.service.IOrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @GetMapping("/")
    public ResponseEntity<CustomResponse> getOrders(){
        log.info("getOrders called..");
        return ResponseEntity.ok(new CustomResponse("get success", orderService.getOrders()));
    }

    @GetMapping("/date/{date}")
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
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new CustomResponse("get success", orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createOrder(@RequestBody CreateOrderRequest request){
        log.info("createOrder called..");
        try {
            Order order = orderService.createOrder(request);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new CustomResponse("create success: " + order.getId(), orderDto));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updateOrderById(@PathVariable("id") Long id, @RequestBody UpdateOrderRequest request){
        log.info("updateOrderById called..");
        try {
            Order order = orderService.updateOrder(id, request);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new CustomResponse("update success", orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomResponse> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        log.info("updateOrderStatus called..");
        try {
            String newStatus = payload.get("status");
            orderService.updateStatus(id, newStatus);
            return ResponseEntity.ok(new CustomResponse("update status success", null));
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
