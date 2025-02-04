package com.hvrc.bookStore.kafka;

import com.hvrc.bookStore.smsService.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookEventConsumer {

    @Autowired
    private SmsService smsService;

    @KafkaListener(topics = "book-added", groupId = "bookstore-group")
    public void consumeBookAddedEvent(String message) {
        System.out.println("Received Kafka event: " + message);
    }

    @KafkaListener(topics = "book-inventory-update", groupId = "bookstore-group")
    public void consumeInventoryUpdate(String message) {
        System.out.println("Inventory update received: " + message);

        String[] data = message.split(",");
        String eventType = data[0];
        String bookName = data[1];
        String phoneNumber = data[2];

        String smsMessage;
        if ("SELL".equals(eventType)) {
            smsMessage = "Your book '" + bookName + "' has been sold!";
        } else {
            smsMessage = "Your book '" + bookName + "' has been rented!";
        }

        smsService.sendSms(phoneNumber, smsMessage);
    }
}
