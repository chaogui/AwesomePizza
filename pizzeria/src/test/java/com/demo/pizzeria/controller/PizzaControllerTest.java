package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.repository.PizzaRepository;
import com.demo.pizzeria.repository.ToppingRepository;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import com.demo.pizzeria.service.IPizzaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
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

@WebMvcTest(PizzaController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    PizzaRepository pizzaRepository;

    @Mock
    ToppingRepository toppingRepository;

    @InjectMocks
    IPizzaService pizzaService;

    @Autowired
    private ObjectMapper objectMapper;

    Pizza pizza;

    @BeforeEach
    public void setup(){

        pizza = new Pizza();
        pizza.setId(1L);
        pizza.setName("Diavola");
        pizza.setPrice(BigDecimal.valueOf(10.00));
        //pizza.setToppings();
    }

    //Post Controller
    @Test
    @Order(1)
    public void savePizzaTest() throws Exception, ResourceAlreadyExistsException {
        // precondition
        given(pizzaService.addPizza(any(CreatePizzaRequest.class))).willReturn(pizza);

        // action
        ResultActions response = mockMvc.perform(post("/pizza")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pizza)));

        // verify
        response.andDo(print())
                .andExpect(jsonPath("$.name", is(pizza.getName())))
                .andExpect(jsonPath("$.description", is(pizza.getDescription())))
                .andExpect(jsonPath("$.id", is(pizza.getId())));
    }

    //Get Controller
    @Test
    @Order(2)
    public void getPizzaTest() throws Exception{
        // precondition
        List<Pizza> list = new ArrayList<>();
        list.add(pizza);
//        pizza = new Pizza();
//        pizza.setId(1L);
//        pizza.setName("Diavola");
//        pizza.setPrice(BigDecimal.valueOf(10.00));
        //list.add(Pizza.builder().id(2L).firstName("Sam").lastName("Curran").email("sam@gmail.com").build());
        given(pizzaService.getAllPizza()).willReturn(list);

        // action
        ResultActions response = mockMvc.perform(get("/api/Pizzas"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(list.size())));

    }

    //get by Id controller
    @Test
    @Order(3)
    public void getByIdPizzaTest() throws Exception{
        // precondition
        given(pizzaService.getPizzaById(pizza.getId())).willReturn(pizza);

        // action
        ResultActions response = mockMvc.perform(get("/api/Pizzas/{id}", pizza.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(pizza.getName())))
                .andExpect(jsonPath("$.description", is(pizza.getDescription())))
                .andExpect(jsonPath("$.id", is(pizza.getId())));

    }


    //Update Pizza
    @Test
    @Order(4)
    public void updatePizzaTest() throws Exception{
        // precondition
        given(pizzaService.getPizzaById(pizza.getId())).willReturn(pizza);
        pizza.setName("Margherita");
        pizza.setDescription("Margherita description");
        given(pizzaService.updatePizza(pizza.getId(), new UpdatePizzaRequest())).willReturn(pizza);

        // action
        ResultActions response = mockMvc
                .perform(put("/api/Pizzas/{id}", pizza.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pizza)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(pizza.getName())))
                .andExpect(jsonPath("$.description", is(pizza.getDescription())));
    }


    // delete Pizza
    @Test
    public void deletePizzaTest() throws Exception{
        // precondition
        willDoNothing().given(pizzaService).deletePizzaById(pizza.getId());

        // action
        ResultActions response = mockMvc.perform(delete("/pizza/{id}", pizza.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}