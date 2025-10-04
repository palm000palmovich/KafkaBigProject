package com.example.Client.consumer;

import com.example.Client.services.NewItemsService;
import com.example.Shop.dto.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NewItemsConsumer {
    private Logger logger = LoggerFactory.getLogger(NewItemsConsumer.class);
    private final NewItemsService newItemsService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(
            topics = "${app.kafka.topics.input}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeNewItem(Item item,
                               @Header(KafkaHeaders.RECEIVED_KEY) String key,
                               Acknowledgment ack) {
        try {
            logger.info("Received: item {}", item);
            newItemsService.saveNewItem(item);
            logger.info("Item {} processed successfully", item.getProductId());

            ack.acknowledge(); //Подтверждаем только при успехе
        } catch (Exception e) {
            logger.error("Failed to process item {}: {}", item.getProductId(), e.getMessage());

            // Отправка в Dead Letter Queue
            sendToDlq(key, item, e.getMessage());
        }

    }

    private void sendToDlq(String key, Item item, String error) {
        logger.error("Sending consumer error...");
        Map<String, Object> dlqMessage = new HashMap<>();
        dlqMessage.put("originalItem", item);
        dlqMessage.put("error", error);
        dlqMessage.put("timestamp", LocalDateTime.now());
        kafkaTemplate.send("new_items_dlq", key, dlqMessage);
        logger.info("Item {} sent to DLQ", key);
    }

}
