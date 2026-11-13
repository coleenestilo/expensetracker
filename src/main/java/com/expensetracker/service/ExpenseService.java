package com.expensetracker.service;

import com.expensetracker.model.Expense;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();

    public ExpenseService() {
        addExpense(new Expense(null, "Groceries", new BigDecimal("1250.50"),
                "Food", LocalDate.now()));
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public Optional<Expense> getExpenseById(String id) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Expense addExpense(Expense input) {
        Expense saved = new Expense(UUID.randomUUID().toString(),
                input.getDescription(),
                input.getAmount(),
                input.getCategory(),
                input.getDate());
        expenses.add(saved);
        return saved;
    }

    public Optional<Expense> updateExpense(String id, Expense input) {
        return getExpenseById(id).map(existing -> {
            existing.setDescription(input.getDescription());
            existing.setAmount(input.getAmount());
            existing.setCategory(input.getCategory());
            existing.setDate(input.getDate());
            return existing;
        });
    }

    public boolean deleteExpense(String id) {
        return expenses.removeIf(e -> e.getId().equals(id));
    }
}

