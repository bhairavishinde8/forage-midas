package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository,
                              IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        // Validate sender exists
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return false; // Invalid sender or recipient
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            return false; // Insufficient funds
        }

        // Get incentive amount from API
        float incentiveAmount = incentiveService.getIncentiveAmount(transaction);

        // Process the transaction with incentive
        float transactionAmount = transaction.getAmount();

        // Deduct only transaction amount from sender
        sender.setBalance(sender.getBalance() - transactionAmount);

        // Add both transaction amount AND incentive to recipient
        recipient.setBalance(recipient.getBalance() + transactionAmount + incentiveAmount);

        // Save updated user balances
        userRepository.save(sender);
        userRepository.save(recipient);

        // Create and save transaction record with incentive
        TransactionRecord transactionRecord = new TransactionRecord(
                sender, recipient, transactionAmount, incentiveAmount
        );
        transactionRecordRepository.save(transactionRecord);

        System.out.println("Processed transaction: " + transactionAmount +
                " with incentive: " + incentiveAmount);

        return true; // Transaction successful
    }

    // Method to get user balance by name (for debugging "wilbur")
    public Float getUserBalance(String userName) {
        return userRepository.findByName(userName)
                .map(UserRecord::getBalance)
                .orElse(null);
    }

    // Debug method for wilbur
    public void debugWilburBalance() {
        try {
            UserRecord wilbur = userRepository.findByName("wilbur").get();
            System.out.println("=== DEBUG WILBUR ===");
            System.out.println("Wilbur balance: " + wilbur.getBalance());
            System.out.println("Rounded down: " + (int) wilbur.getBalance());
            System.out.println("=== DEBUG WILBUR ===");
        } catch (Exception e) {
            System.out.println("Wilbur not found in database");
        }
    }

    public Float getUserBalanceById(Long userId) {
        return userRepository.findById(userId)
                .map(UserRecord::getBalance)
                .orElse(null);
    }
}