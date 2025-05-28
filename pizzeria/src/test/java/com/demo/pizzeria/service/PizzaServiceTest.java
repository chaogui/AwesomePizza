package com.demo.pizzeria.service;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.data.Topping;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.repository.ToppingRepository;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;
    @Mock
    private ToppingRepository toppingRepository;

    @InjectMocks
    private PizzaService pizzaService;

    private Pizza pizza;
    private Topping topping, topping1;


    @BeforeEach
    public void setup(){

        pizza = new Pizza();
        pizza.setId(1L);
        pizza.setName("Diavola");
        pizza.setDescription("Diavola Desc");
        pizza.setPrice(BigDecimal.valueOf(10.00));
        //pizza.setToppings();

        topping = new Topping();
        topping.setId(1L);
        topping.setName("Pomodoro");

        topping1 = new Topping();
        topping1.setId(1L);
        topping1.setName("Salame Piccante");

        pizza.setToppings(Set.of(topping1));

    }

    @Test
    @Order(1)//test OK
    public void addPizzaTest() throws ResourceAlreadyExistsException {

        Pizza pizza1 = new Pizza();
        pizza1.setId(1L);
        pizza1.setName("Diavola");
        pizza1.setDescription("Diavola Desc");
        pizza1.setPrice(BigDecimal.valueOf(10.00));

        Topping topping1 = new Topping();
        topping1.setName("Salame Piccante");

        pizza1.setToppings(Set.of(topping1));

        // precondition
        //given(pizzaRepository.save(pizza1)).willReturn(pizza1);
        when(pizzaRepository.save(Mockito.any(Pizza.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        given(pizzaRepository.existsByName(pizza1.getName())).willReturn(false);


        //given(toppingRepository.save(topping1)).willReturn(topping1);
        given(toppingRepository.findByName(topping1.getName())).willReturn(Optional.of(topping1));

        CreatePizzaRequest request = new CreatePizzaRequest();
        request.setName(pizza1.getName());
        request.setDescription(pizza1.getDescription());
        request.setPrice(pizza1.getPrice());
        request.setToppingNames(Set.of(topping1.getName()));

        //action
        Pizza savedPizza = pizzaService.addPizza(request);

        // verify the output
        assertThat(savedPizza).isNotNull();
    }

    @Test
    @Order(2)//test OK
    public void getPizzaById() throws ResourceNotFoundException {
        // precondition
        given(pizzaRepository.findById(1L)).willReturn(Optional.ofNullable(pizza));

        // action
        Pizza existingPizza = pizzaService.getPizzaById(pizza.getId());

        // verify
        assertThat(existingPizza).isNotNull();
        assertThat(existingPizza.getName()).isEqualTo("Diavola");

    }


    @Test
    @Order(3) //test OK
    public void getAllPizza(){
        Pizza pizza1 = new Pizza();
        pizza1.setId(2L);
        pizza1.setName("Margherita");
        pizza1.setPrice(BigDecimal.valueOf(10.00));

        // precondition
        given(pizzaRepository.findAll()).willReturn(List.of(pizza,pizza1));

        // action
        List<Pizza> PizzaList = pizzaService.getAllPizza();

        // verify
        assertThat(PizzaList).isNotNull();
        assertThat(PizzaList.size()).isGreaterThan(1);
    }

    @Test
    @Order(4)//test OK
    public void updatePizza() throws ResourceNotFoundException {

        // precondition
        given(pizzaRepository.findById(pizza.getId())).willReturn(Optional.of(pizza));
        given(pizzaRepository.save(pizza)).willReturn(pizza);
        given(toppingRepository.findByName(topping.getName())).willReturn(Optional.of(topping));

        UpdatePizzaRequest request = new UpdatePizzaRequest();
        request.setName("Margherita");
        request.setDescription("Margherita description");
        request.setToppingNames(Set.of("Pomodoro"));

        // action
        Pizza updatedPizza = pizzaService.updatePizza(pizza.getId(), request);

        // verify
        assertThat(updatedPizza.getName()).isEqualTo("Margherita");
        assertThat(updatedPizza.getDescription()).isEqualTo("Margherita description");
    }

    @Test
    @Order(5)//test OK
    public void deletePizza() throws ResourceNotFoundException {

        // precondition
        willDoNothing().given(pizzaRepository).deleteById(pizza.getId());
        given(pizzaRepository.findById(pizza.getId())).willReturn(Optional.of(pizza));

        // action
        pizzaService.deletePizzaById(pizza.getId());

        // verify
        verify(pizzaRepository, times(1)).deleteById(pizza.getId());
    }


}