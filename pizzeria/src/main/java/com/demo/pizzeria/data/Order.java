package com.demo.pizzeria.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdTime;
    private BigDecimal totalPrice = BigDecimal.ZERO;

//    @Enumerated(EnumType.STRING)
//    private OrderStatus orderStatus;

    private String status;
    private LocalDateTime completedTime;
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}