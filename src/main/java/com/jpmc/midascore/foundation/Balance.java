package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    private float amount;  // CHANGE BACK TO 'amount' to match toString()

    public Balance() {
    }

    public Balance(float amount) {
        this.amount = amount;
    }

    @JsonProperty("amount")
    public float getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(float amount) {
        this.amount = amount;
    }

    // DO NOT MODIFY THIS toString() METHOD
    @Override
    public String toString() {
        return "Balance {amount=" + amount + "}";
    }
}