package com.demo.pizzeria.controller;

import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.request.CreatePizzaRequest;
import com.demo.pizzeria.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PizzaController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PizzaService pizzaService;  // standard mock

    @TestConfiguration
    static class TestConfig {
        @Bean
        PizzaService pizzaService(OrderControllerTest testInstance) {
            return testInstance.pizzaService; // inject mock into Spring context
        }
    }

    @Test
    void createPizza_returnsCreatedPizza() throws Exception {
        Pizza mockPizza = new Pizza();
        mockPizza.setId(1L);
        mockPizza.setName("Test Pizza");

        CreatePizzaRequest request = new CreatePizzaRequest();

//        when(pizzaService.addPizza(eq("Test Pizza"), anySet()))
//                .thenReturn(mockPizza);

        mockMvc.perform(post("/pizzas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Test Pizza",
                        "toppingNames": ["Cheese", "Basil"]
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Pizza"));
    }
}
