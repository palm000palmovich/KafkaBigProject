package com.example.Shop.configuration;

import com.example.Shop.dto.Item;
import com.example.Shop.services.ProductValidationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Collections;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaStreamsConfiguration {
    private Logger logger = LoggerFactory.getLogger(KafkaStreamsConfiguration.class);
    private final ProductValidationService validationService;

    @Value("${app.kafka.topics.input}")
    private String inputTopic;
    @Value("${app.kafka.topics.output}")
    private String outputTopic;
    @Value("${app.kafka.topics.rejected}")
    private String rejectedTopic;

    @Bean
    public KStream<String, Item> productValidationStream(StreamsBuilder streamsBuilder) {
        JsonSerde<Item> itemSerde = new JsonSerde<>(Item.class);
        itemSerde.configure(Collections.singletonMap("spring.json.trusted.packages", "com.example.Shop.dto,*"), false);

        KStream<String, Item> inputStream = streamsBuilder.stream(
                inputTopic,
                Consumed.with(Serdes.String(), itemSerde)
        );

        logger.info("Kafka Streams configured for topic: {}", inputTopic);

        inputStream
                .filter((key, item) -> validationService.isValidProduct(item))
                .peek((key, item) -> logger.info("VALID: Product {} -> new_items", item.getProductId()))
                .to(outputTopic, Produced.with(Serdes.String(), itemSerde));

        inputStream
                .filter((key, item) -> !validationService.isValidProduct(item))
                .peek((key, item) -> logger.error("INVALID: Product {} -> rejected-items", item.getProductId()))
                .to(rejectedTopic, Produced.with(Serdes.String(), itemSerde));

        return inputStream;
    }

}
