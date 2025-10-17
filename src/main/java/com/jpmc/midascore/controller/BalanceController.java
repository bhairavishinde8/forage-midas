package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final TransactionService transactionService;

    public BalanceController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<Balance> getBalance(@RequestParam Long userId) {
        // Get balance from service
        Float balanceAmount = transactionService.getUserBalanceById(userId);

        // If user not found or balance is null, return balance of 0 for that user ID
        if (balanceAmount == null) {
            return ResponseEntity.ok(new Balance(0.0f));
        }

        return ResponseEntity.ok(new Balance(balanceAmount));
    }
}