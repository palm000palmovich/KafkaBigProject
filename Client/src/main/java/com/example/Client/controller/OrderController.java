package com.example.Client.controller;

import com.example.Client.exceptions.SendingTransactionException;
import com.example.Client.model.OrderEntity;
import com.example.Client.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping(path = "/make-an-order/{userId}/{itemId}")
    public ResponseEntity<OrderEntity> saveNewOrder(@PathVariable("userId") Long userId,
                                                    @PathVariable("itemId") Long itemId) {
        try {
            return ResponseEntity.ok(
                    orderService.saveNewOrder(userId, itemId)
            );
        } catch (RuntimeException ex) {
            logger.error("Error: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
