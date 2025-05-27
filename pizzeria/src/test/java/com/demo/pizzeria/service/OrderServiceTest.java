package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.data.Topping;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.repository.ToppingRepository;
import com.demo.pizzeria.request.CreatePizzaRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    PizzaRepository pizzaRepository;

    @Mock
    ToppingRepository toppingRepository;

    @InjectMocks
    PizzaService pizzaService;

    @Test
    void createPizza_withNewToppings_savesCorrectly() throws ResourceAlreadyExistsException {
        // Arrange
        String name = "Margarita";
        Set<String> toppingNames = Set.of("Cheese", "Tomato");

        when(toppingRepository.findByName("Cheese"))
                .thenReturn(Optional.empty());
        when(toppingRepository.save(any(Topping.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(pizzaRepository.save(any(Pizza.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        CreatePizzaRequest request = new CreatePizzaRequest();

        Pizza pizza = pizzaService.addPizza(request);

        // Assert
        assertEquals(name, pizza.getName());
        assertEquals(2, pizza.getToppings().size());
        verify(pizzaRepository).save(any(Pizza.class));
    }
}
