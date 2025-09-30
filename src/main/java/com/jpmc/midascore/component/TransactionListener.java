package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void receiveTransaction(Transaction transaction) {
        logger.info("Processing transaction: {}", transaction);

        boolean success = transactionService.processTransaction(transaction);

        // Call debug after each transaction
        transactionService.debugWaldorfBalance();

        if (success) {
            logger.info("Transaction processed successfully");
        } else {
            logger.warn("Transaction rejected");
        }
    }
}