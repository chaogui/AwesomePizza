package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.data.Topping;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.repository.ToppingRepository;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PizzaService implements IPizzaService{

    private final PizzaRepository pizzaRepository;
    private final ToppingRepository toppingRepository;

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
    @Transactional
    public Pizza addPizza(CreatePizzaRequest request)throws ResourceAlreadyExistsException {

//        if(pizzaRepository.findById(pizza.getId()).isPresent()){
//            throw new ResourceAlreadyExistsException("Pizza with id = "+pizza.getId() +" already exists. You may update the pizza.");
//        }
        if(pizzaRepository.existsByName(request.getName())){
            throw new ResourceAlreadyExistsException("Pizza with name = "+request.getName() +" already exists. You may update the pizza.");
        }
        Pizza pizza = new Pizza();
        pizza.setName(request.getName());
        pizza.setPrice(request.getPrice());
        pizza.setDescription(request.getDescription());

        Set<Topping> toppings = request
                .getToppingNames()
                .stream()
                .map(name -> toppingRepository
                        .findByName(name)
                        .orElseGet(() -> {
                            Topping topping = new Topping();
                            topping.setName(name);
                            return toppingRepository.save(topping);
                        }))
                .collect(Collectors.toSet());
        pizza.setToppings(toppings);

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
        pizza.setPrice(request.getPrice());
        pizza.setDescription(request.getDescription());

        Set<Topping> toppings = request.getToppingNames()
                .stream()
                .map(name -> toppingRepository
                        .findByName(name)
                        .orElseGet(() -> {
                            Topping topping = new Topping();
                            topping.setName(name);
                            return toppingRepository.save(topping);
                        }))
                .collect(Collectors.toSet());
        pizza.setToppings(toppings);//replace toppings: remove old relations and add new relations

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
