package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private com.jpmc.midascore.repository.UserRepository userRepository; // ADD THIS

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }
        Thread.sleep(10000); // Increased sleep to ensure all transactions process

        // ADD THIS DEBUG CODE:
        logger.info("=== CHECKING WILBUR'S BALANCE ===");

        // Get wilbur's balance directly from database
        var wilburOpt = userRepository.findByName("wilbur");
        if (wilburOpt.isPresent()) {
            float wilburBalance = wilburOpt.get().getBalance();
            int roundedBalance = (int) wilburBalance; // Round down to nearest integer

            logger.info("Wilbur's balance: {}", wilburBalance);
            logger.info("Rounded down: {}", roundedBalance);
            logger.info("=== SUBMIT THIS NUMBER: {} ===", roundedBalance);
        } else {
            logger.error("Wilbur not found in database!");
        }

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to find out what wilbur's balance is after all transactions are processed");
        logger.info("kill this test once you find the answer");

        // Keep running so you can see the output
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}