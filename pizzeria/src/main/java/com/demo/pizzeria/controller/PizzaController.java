package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import com.demo.pizzeria.response.CustomResponse;
import com.demo.pizzeria.service.IPizzaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/pizza")
@Slf4j
@AllArgsConstructor
public class PizzaController {

    private final IPizzaService pizzaService;

    @GetMapping("")
    public ResponseEntity<CustomResponse> getAllPizzas(){
        log.info("getAllPizzas called..");
        return ResponseEntity.ok(new CustomResponse("get success", pizzaService.getAllPizza()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getPizzaById(@PathVariable Long id) {
        log.info("getPizzaById called..");
        try {
            Pizza pizza = pizzaService.getPizzaById(id);
            return ResponseEntity.ok(new CustomResponse("get success", pizza));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse("Pizza with id = " + id + " not found", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> addPizza(@RequestBody CreatePizzaRequest request){
        log.info("addPizza called..");
        try {
            //log.info(String.valueOf(pizza.getId()));
            Pizza pizza = pizzaService.addPizza(request);
            return ResponseEntity.ok(new CustomResponse("create success", pizza));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new CustomResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updatePizzaById(@PathVariable("id") Long id, @RequestBody UpdatePizzaRequest request){
        log.info("updatePizzaById called..");
        try {
            Pizza pizza = pizzaService.updatePizza(id, request);
            return ResponseEntity.ok(new CustomResponse("update success", pizza));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse("Pizza with id = " + id + " not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deletePizzaById(@PathVariable("id") Long id){
        log.info("deletePizzaById called..");
        try {
            pizzaService.deletePizzaById(id);
            return ResponseEntity.ok(new CustomResponse("delete success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new CustomResponse("Pizza with id = " + id + " not found", null));
        }
    }
}
