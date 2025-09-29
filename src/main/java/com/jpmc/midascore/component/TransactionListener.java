package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void receiveTransaction(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);

        // SET A BREAKPOINT HERE!
        // Use debugger to see the transaction amounts
        // The amount is: transaction.getAmount()
    }
}