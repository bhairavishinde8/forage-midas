package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class IncentiveService {

    private final RestTemplate restTemplate;
    private final String incentiveApiUrl = "http://localhost:8080/incentive";

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float getIncentiveAmount(Transaction transaction) {
        try {
            // Send transaction to incentive API and get response
            ResponseEntity<Incentive> response = restTemplate.postForEntity(
                    incentiveApiUrl,
                    transaction,
                    Incentive.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getAmount();
            }
        } catch (Exception e) {
            System.err.println("Error calling incentive API: " + e.getMessage());
        }

        return 0.0f; // Default to 0 if API call fails
    }
}