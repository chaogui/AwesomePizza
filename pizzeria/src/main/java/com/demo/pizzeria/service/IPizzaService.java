package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;

import java.util.List;

public interface IPizzaService {
    Pizza getPizzaById(Long id) throws ResourceNotFoundException;
    Pizza getPizzaByName(String name) throws ResourceNotFoundException;
    List<Pizza> getAllPizza();
    Pizza addPizza(CreatePizzaRequest request) throws ResourceAlreadyExistsException;
    Pizza updatePizza(Long id, UpdatePizzaRequest request) throws ResourceNotFoundException;
    void deletePizzaById(Long id) throws ResourceNotFoundException;
}
