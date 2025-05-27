package com.astonlabs.service;

import com.astonlabs.dto.UserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Value("${kafka.topic.user-events}")
    private String topic;

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(UserEvent event) {
        kafkaTemplate.send(topic, event);
        System.out.println("Sent to Kafka: " + event);
    }
}

