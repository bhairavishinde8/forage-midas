package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        // Parse the string according to your Transaction class
        String[] transactionData = transactionLine.split(", ");

        // Create Transaction object with correct parameters
//        long senderId = Long.parseLong(transactionData[0]);
//        long recipientId = Long.parseLong(transactionData[1]);
//        float amount = Float.parseFloat(transactionData[2]);

        //Transaction transaction = new Transaction(senderId, recipientId, amount);
        //kafkaTemplate.send(topic, transaction);
        kafkaTemplate.send(topic, new Transaction(Long.parseLong(transactionData[0]), Long.parseLong(transactionData[1]), Float.parseFloat(transactionData[2])));
    }
}