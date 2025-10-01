package com.example.Client.consumer;

import com.example.Client.services.NewItemsService;
import com.example.Shop.dto.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewItemsConsumer {
    private Logger logger = LoggerFactory.getLogger(NewItemsConsumer.class);
    private final NewItemsService newItemsService;

    @KafkaListener(
            topics = "${app.kafka.topics.input}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeNewItem(Item item) {
        logger.info("Received: item {}", item);
        newItemsService.saveNewItem(item);
    }
}
