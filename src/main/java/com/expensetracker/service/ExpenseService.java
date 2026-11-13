package com.expensetracker.service;

import com.expensetracker.model.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {

    private final List<Expense> expenses = new ArrayList<>();
    private final AccountService accountService;

    public ExpenseService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public Optional<Expense> getExpenseById(String id) {
        return expenses.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public Expense addExpense(Expense input) {
        // subtract from the account (400 if the account is not found)
        boolean found = accountService.adjustBalance(
                input.getAccountId(), input.getAmount().negate());
        if (!found) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }

        Expense saved = new Expense(UUID.randomUUID().toString(),
                input.getDescription(),
                input.getAmount(),
                input.getCategory(),
                input.getAccountId(),
                input.getDate() != null ? input.getDate() : LocalDate.now());
        expenses.add(saved);
        return saved;
    }

    public Optional<Expense> updateExpense(String id, Expense input) {
        Optional<Expense> found = getExpenseById(id);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        if (accountService.getAccountById(input.getAccountId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        Expense existing = found.get();

        // 1. give the old expense's money back
        accountService.adjustBalance(existing.getAccountId(), existing.getAmount());
        // 2. charge the new expense
        accountService.adjustBalance(input.getAccountId(), input.getAmount().negate());

        existing.setDescription(input.getDescription());
        existing.setAmount(input.getAmount());
        existing.setCategory(input.getCategory());
        existing.setAccountId(input.getAccountId());
        existing.setDate(input.getDate());
        return Optional.of(existing);
    }

    public boolean deleteExpense(String id) {
        Optional<Expense> found = getExpenseById(id);
        if (found.isEmpty()) {
            return false;
        }
        Expense expense = found.get();

        // deleting an expense gives the money back
        accountService.adjustBalance(expense.getAccountId(), expense.getAmount());
        expenses.remove(expense);
        return true;
    }
}