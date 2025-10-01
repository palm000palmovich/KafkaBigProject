package com.example.Client.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {
    @Value("${app.kafka.topics.input}")
    private String inputTopic;
    @Value("${app.kafka.topics.output}")
    private String outputTopic;

    @Bean
    public NewTopic newItemsTopic() {
        return TopicBuilder.name(inputTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic orderedItemsTopic() {
        return TopicBuilder.name(outputTopic)
                .partitions(3)
                .replicas(2)
                .build();
    }
}
