package com.astonlabs.consumer;

import com.astonlabs.dto.UserEvent;
import com.astonlabs.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final NotificationService notificationService;

    public KafkaConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${kafka.topic.user-events}",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory" // обязательно указать кастомный factory
    )
    public void handleUserEvent(UserEvent event) {
        System.out.println("Received event from Kafka: " + event);
        notificationService.sendNotification(event);
    }
}