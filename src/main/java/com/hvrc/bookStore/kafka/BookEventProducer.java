package com.hvrc.bookStore.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookEventProducer {

    private static final String TOPIC = "book-added";
    private static final String TOPIC2 = "book-inventory-update";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendBookAddedEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    public void sendBookInventoryEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
