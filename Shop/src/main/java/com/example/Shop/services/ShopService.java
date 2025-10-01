package com.example.Shop.services;

import com.example.Shop.dto.Item;

import com.example.Shop.exceptions.CensoreException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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

        //В любом случае отправляем в inputTopic
        Item item1 = sendItemToInputTopic(item);

        //Потом в случае невалидного товара выкидываем CensoreException
        if (!productValidationService.isValidProduct(item)){
            throw new CensoreException("Invalid item");
        }

        return item1;
    }

    private Item sendItemToInputTopic(Item item) {
        logger.info("Попытка отправки нового айтема в топик {}.", inputTopic);
        try {
            // Синхронная отправка с ожиданием результата
            SendResult<String, Item> result = kafkaTemplate.send(inputTopic, item.getProductId(), item)
                    .get(); // Ждем подтверждения

            logger.info("Item {} successfully sent to Kafka. Partition: {}, Offset: {}",
                    item.getProductId(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

            return item;
        } catch (Exception e) {
            logger.error("Error sending to kafka topic: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
