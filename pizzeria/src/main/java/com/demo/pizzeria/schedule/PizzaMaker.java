package com.demo.pizzeria.schedule;

import com.demo.pizzeria.data.Order;
import com.demo.pizzeria.data.OrderItem;
import com.demo.pizzeria.data.Pizza;
import com.demo.pizzeria.exception.ResourceNotFoundException;
import com.demo.pizzeria.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PizzaMaker {

    private final OrderService orderService;

    @Scheduled(cron = "*/10 * * * * *")
    @Transactional
    public void makePizza() throws ResourceNotFoundException, InterruptedException {

        Order order = orderService.findOrderToWork();
        if(order == null){
            log.info("No orders to work.");
            return;
        }
        log.info("Start working order: " + order.getId());

        Pizza pizza;
        for(OrderItem item: order.getItems()){
            pizza = item.getPizza();
            for(int i=1; i<= item.getQuantity();i++){
                log.info("Making pizza: " + pizza.getName() + " - " + i);
                Thread.sleep(2000); // making pizza
            }
        }
        orderService.updateStatus(order.getId(), "PREPARED");
        log.info("Finished order: " + order.getId());
    }
}
