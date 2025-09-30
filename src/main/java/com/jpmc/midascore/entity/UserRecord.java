package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_record")
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private float balance;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<TransactionRecord> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<TransactionRecord> receivedTransactions = new ArrayList<>();

    // Constructors, getters, and setters
    public UserRecord() {}

    public UserRecord(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }

    public List<TransactionRecord> getSentTransactions() { return sentTransactions; }
    public List<TransactionRecord> getReceivedTransactions() { return receivedTransactions; }
}