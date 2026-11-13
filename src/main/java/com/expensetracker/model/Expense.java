package com.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Expense {
    // Fields for each expense
    private final String id;
    private String description;
    private BigDecimal amount;
    private String category;
    private String accountId;
    private LocalDate date;

    @Size(max = 100, message = "Description must be 100 characters or less")

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount can have at most 2 decimal places")

    @NotBlank(message = "Category is required")

    @NotBlank(message = "Account is required")

    public Expense(String id, String description, BigDecimal amount, String category, String accountId, LocalDate date) {
        // Constructor
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.accountId = accountId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getAccountId() {
        return accountId;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
