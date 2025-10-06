package com.example.Shop.services;

import com.example.Shop.dto.Item;

import com.example.Shop.exceptions.CensoreException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShopService {
    private Logger logger = LoggerFactory.getLogger(ShopService.class);
    private final KafkaTemplate<String, Item> kafkaTemplate;
    private final ProductValidationService productValidationService;

    @Value("${app.kafka.topics.input}")
    private String inputTopic;

    public Item sendNewItem(Item item) {

        if (item.getCreatedAt() == null) {
            item.setCreatedAt(LocalDateTime.now());
        }
        if (item.getUpdatedAt() == null) {
            item.setUpdatedAt(LocalDateTime.now());
        }

        kafkaTemplate.send(inputTopic, item.getProductId(), item)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        logger.error("Failed to send item {} to Kafka: {}",
                                item.getProductId(), exception.getMessage());
                    } else {
                        logger.info("Item {} successfully sent to Kafka. Partition: {}, Offset: {}",
                                item.getProductId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });

        //Потом в случае невалидного товара выкидываем CensoreException
        if (!productValidationService.isValidProduct(item)){
            throw new CensoreException("Invalid item");
        }

        return item;
    }

}
