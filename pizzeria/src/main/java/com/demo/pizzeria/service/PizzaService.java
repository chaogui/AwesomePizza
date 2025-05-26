package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PizzaService implements IPizzaService{

    private final PizzaRepository pizzaRepository;

    @Override
    public Pizza getPizzaById(Long id) throws ResourceNotFoundException {
        return pizzaRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Pizza with id = " + id +" not found."));
    }

    @Override
    public Pizza getPizzaByName(String name) throws ResourceNotFoundException {
        return pizzaRepository
                .findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Pizza with name = " + name +" not found."));
    }

    @Override
    public List<Pizza> getAllPizza() {
        return pizzaRepository.findAll();
    }

    @Override
    public Pizza addPizza(Pizza pizza) throws ResourceAlreadyExistsException {
        if(pizzaRepository.findById(pizza.getId()).isPresent()){
            throw new ResourceAlreadyExistsException("Pizza with id = "+pizza.getId() +" already exists. You may update the pizza.");
        }
        if(pizzaRepository.existsByName(pizza.getName())){
            throw new ResourceAlreadyExistsException("Pizza with name = "+pizza.getName() +" already exists. You may update the pizza.");
        }
        return pizzaRepository.save(pizza);
    }

    @Override
    public Pizza updatePizza(Long id, UpdatePizzaRequest request) throws ResourceNotFoundException {
        return pizzaRepository
                .findById(id)
                .map(pizzaInDb -> updateExistingPizza(pizzaInDb, request))
                .map(pizzaRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("Pizza with id = " + id +" not found."));
    }

    public Pizza updateExistingPizza(Pizza pizza, UpdatePizzaRequest request) {
        pizza.setName(request.getName());
        //pizza.setToppings(request.getToppings());
        pizza.setPrice(request.getPrice());
        return pizza;
    }

    @Override
    public void deletePizzaById(Long id) throws ResourceNotFoundException {
        if(getPizzaById(id) == null){
            throw new ResourceNotFoundException("Pizza with id = " + id +" not found.");
        }
        pizzaRepository.deleteById(id);
    }
}
