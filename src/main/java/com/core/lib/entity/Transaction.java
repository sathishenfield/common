package com.core.lib.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "txn_date", nullable = false)
    private Instant txnDate;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status;

    @Column(name = "category")
    private String category;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "channel")
    private String channel;

    @Column(name = "reward_points")
    private Integer rewardPoints;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "tax_amount")
    private double taxAmount;
}
