package com.astonlabs.controller;

import com.astonlabs.dto.UserEvent;
import com.astonlabs.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public String sendNotification(@RequestBody UserEvent event) {
        notificationService.sendNotification(event);
        return "Email sent successfully";
    }
}