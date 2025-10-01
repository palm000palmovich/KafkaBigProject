package com.example.Shop.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${app.kafka.topics.input}")
    private String inputTopic;
    @Value("${app.kafka.topics.output}")
    private String outputTopic;
    @Value("${app.kafka.topics.rejected}")
    private String rejectedTopic;

    @Bean
    public NewTopic productsInputTopic() {
        return TopicBuilder.name(inputTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic newItemsTopic() {
        return TopicBuilder.name(outputTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic rejectedItemsTopic() {
        return TopicBuilder.name(rejectedTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }

}
