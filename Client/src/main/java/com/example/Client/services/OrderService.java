package com.example.Client.services;


import com.example.Client.exceptions.SendingTransactionException;
import com.example.Client.model.ItemEntity;
import com.example.Client.model.OrderEntity;
import com.example.Client.repositories.ItemEntityRepository;
import com.example.Client.repositories.OrderEntityRepository;
import common.vars.dto.OrderEvent;
import jakarta.transaction.Transactional;
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

    @Transactional
    public OrderEntity saveNewOrder(Long userId, Long itemId) {
        ItemEntity foundItem = itemEntityRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found."));
        logger.info("Найденный айтем: {}", foundItem);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setItem(foundItem);

        OrderEntity savedOrder = orderEntityRepository.save(orderEntity);

        kafkaTemplate.executeInTransaction(operations -> {
            OrderEvent orderEvent = createOrderEvent(savedOrder.getId(), userId,
                    savedOrder.getCreatedAt(), foundItem.getProductId());

            // Асинхронная отправка внутри транзакции
            return operations.send(orderedItemsTopic, orderEvent)
                    .thenApply(result -> {
                        logger.info("Order event sent. Partition: {}, Offset: {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                        return result;
                    })
                    .exceptionally(throwable -> {
                        // Если ошибка - транзакция откатится
                        throw new SendingTransactionException("Kafka transaction failed: "
                                + throwable.getMessage());
                    });
        });

        logger.info("Новый заказ успешно сохранен в БД.");
        return savedOrder;

    }

    private OrderEvent createOrderEvent(Long orderId, Long userId,
                                        LocalDateTime orderTime, String productId) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(orderId);
        orderEvent.setUserId(userId);
        orderEvent.setCreatedAt(orderTime);
        orderEvent.setProductId(productId);

        return orderEvent;
    }

}
