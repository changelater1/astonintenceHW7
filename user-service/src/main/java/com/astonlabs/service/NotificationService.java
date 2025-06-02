package com.astonlabs.service;

import com.astonlabs.dto.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendNotification(UserEvent event) {
        String subject = "";
        String text = "";

        if ("created".equals(event.getOperation())) {
            subject = "Ваш аккаунт создан";
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if ("deleted".equals(event.getOperation())) {
            subject = "Аккаунт удален";
            text = "Здравствуйте! Ваш аккаунт был удален.";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
        System.out.println("Email sent to: " + event.getEmail());
    }
}