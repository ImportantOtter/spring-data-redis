package com.example.config.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "message", groupId = "groupId")
    void listener(String message) {
        System.out.println("Listener received: " + message);
    }
}