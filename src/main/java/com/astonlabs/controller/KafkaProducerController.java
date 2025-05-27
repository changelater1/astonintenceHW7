package com.astonlabs.controller;

import com.astonlabs.dto.UserEvent;
import com.astonlabs.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaProducerController {

    private final KafkaProducer kafkaProducer;

    @Autowired
    public KafkaProducerController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/send-event")
    public String sendUserEvent(@RequestBody UserEvent userEvent) {
        kafkaProducer.sendUserEvent(userEvent);
        return "Event sent to Kafka successfully";
    }
}
