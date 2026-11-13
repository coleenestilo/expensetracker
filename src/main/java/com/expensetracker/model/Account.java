package com.expensetracker.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Account {

    private final String id;
    private String name;
    private BigDecimal balance;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be 50 characters or less")

    @NotNull(message = "Balance is required")
    @Digits(integer = 10, fraction = 2, message = "Balance can have at most 2 decimal places")

    public Account(String id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}