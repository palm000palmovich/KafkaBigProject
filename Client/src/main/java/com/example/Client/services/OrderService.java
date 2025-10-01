package com.example.Client.services;


import com.example.Client.model.ItemEntity;
import com.example.Client.model.OrderEntity;
import com.example.Client.repositories.ItemEntityRepository;
import com.example.Client.repositories.OrderEntityRepository;
import common.vars.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final OrderEntityRepository orderEntityRepository;
    private final ItemEntityRepository itemEntityRepository;

    @Value("${app.kafka.topics.output}")
    private String orderedItemsTopic;

    public OrderEntity saveNewOrder(Long userId, Long itemId) {
        ItemEntity foundItem = itemEntityRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found."));
        logger.info("Найденный айтем: {}", foundItem);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setItem(foundItem);

        try {
            OrderEntity savedOrder = orderEntityRepository.save(orderEntity);
            logger.info("Новый заказ успешно сохранен в БД.");

            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrderId(savedOrder.getId());
            orderEvent.setUserId(userId);
            orderEvent.setCreatedAt(savedOrder.getCreatedAt());
            orderEvent.setProductId(foundItem.getProductId());
            logger.info("Информация по заказу для отправки в топик: {}", orderEvent);
            kafkaTemplate.send(orderedItemsTopic, orderEvent);
            logger.info("Информация по заказу {} отправлена в топик.", orderEvent);

            return savedOrder;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
