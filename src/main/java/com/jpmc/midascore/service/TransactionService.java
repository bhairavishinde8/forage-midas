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

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        // Try to find users by ID first, then by name if needed
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

        // If not found by ID, the test might be using different identifiers
        // For now, let's assume the IDs in the Transaction match the database IDs
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return false; // Invalid sender or recipient
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            return false; // Insufficient funds
        }

        // Process the transaction
        float amount = transaction.getAmount();
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        // Save updated user balances
        userRepository.save(sender);
        userRepository.save(recipient);

        // Create and save transaction record
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount);
        transactionRecordRepository.save(transactionRecord);

        return true; // Transaction successful
    }

    // Method to get user balance by name (for debugging "waldorf")
    public Float getUserBalance(String userName) {
        return userRepository.findByName(userName)
                .map(UserRecord::getBalance)
                .orElse(null);
    }

    // Add this method to TransactionService class
    public void debugWaldorfBalance() {
        try {
            UserRecord waldorf = userRepository.findByName("waldorf").get();
            System.out.println("=== DEBUG ===");
            System.out.println("Waldorf balance: " + waldorf.getBalance());
            System.out.println("Rounded down: " + (int) waldorf.getBalance());
            System.out.println("=== DEBUG ===");
        } catch (Exception e) {
            System.out.println("Waldorf not found in database");
        }
    }
}