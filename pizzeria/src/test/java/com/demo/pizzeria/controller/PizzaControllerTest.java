package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceAlreadyExistsException;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.request.UpdatePizzaRequest;
import com.demo.pizzeria.service.IPizzaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    IPizzaService pizzaService;

    @Autowired
    private ObjectMapper objectMapper;

    Pizza pizza;

    @BeforeEach
    public void setup(){

        pizza = new Pizza();
        pizza.setId(1L);
        pizza.setName("Margherita");
        pizza.setDescription("Margherita Desc");
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
                .andExpect(jsonPath("$.data.name", is(pizza.getName())))
                .andExpect(jsonPath("$.data.description", is(pizza.getDescription())))
                .andExpect(jsonPath("$.data.id", is(pizza.getId().intValue())));
    }

    //Get Controller
    @Test
    @Order(2)//test OK
    public void getPizzaTest() throws Exception{
        // precondition
        List<Pizza> list = new ArrayList<>();
        list.add(pizza);

        Pizza pizza1 = new Pizza();
        pizza1.setId(1L);
        pizza1.setName("Diavola");
        pizza1.setPrice(BigDecimal.valueOf(10.00));
        list.add(pizza1);

        given(pizzaService.getAllPizza()).willReturn(list);

        // action
        ResultActions response = mockMvc.perform(get("/pizza"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.size()",is(list.size())));

    }

    //get by Id controller
    @Test
    @Order(3)//test OK
    public void getPizzaByIdTest() throws Exception{
        // precondition
        given(pizzaService.getPizzaById(pizza.getId())).willReturn(pizza);

        // action
        ResultActions response = mockMvc.perform(get("/pizza/{id}", pizza.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.name", is(pizza.getName())))
                .andExpect(jsonPath("$.data.description", is(pizza.getDescription())))
                .andExpect(jsonPath("$.data.id", is(pizza.getId().intValue())));

    }


    //Update Pizza
    @Test
    @Order(4)//test OK
    public void updatePizzaTest() throws Exception{
        // precondition
        given(pizzaService.getPizzaById(pizza.getId())).willReturn(pizza);

        UpdatePizzaRequest request = new UpdatePizzaRequest();
        request.setName("Margherita");
        request.setDescription("Margherita description");

        pizza.setDescription(request.getDescription());
        pizza.setName(request.getName());

        given(pizzaService.updatePizza(pizza.getId(), request)).willReturn(pizza);

        // action
        ResultActions response = mockMvc
                .perform(put("/pizza/{id}", pizza.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.name", is(request.getName())))
                .andExpect(jsonPath("$.data.description", is(request.getDescription())));
    }


    // delete Pizza
    @Test
    @Order(5)//test OK
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