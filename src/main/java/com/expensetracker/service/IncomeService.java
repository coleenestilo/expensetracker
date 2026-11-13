package com.expensetracker.service;

import com.expensetracker.model.Income;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IncomeService {

    private final List<Income> incomes = new ArrayList<>();
    private final AccountService accountService;

    public IncomeService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<Income> getAllIncomes() {
        return incomes;
    }

    public Optional<Income> getIncomeById(String id) {
        return incomes.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public Income addIncome(Income input) {
        // add to the account (400 if the account is not found)
        boolean found = accountService.adjustBalance(
                input.getAccountId(), input.getAmount());
        if (!found) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }

        Income saved = new Income(UUID.randomUUID().toString(),
                input.getDescription(),
                input.getAmount(),
                input.getCategory(),
                input.getAccountId(),
                input.getDate() != null ? input.getDate() : LocalDate.now());
        incomes.add(saved);
        return saved;
    }

    public Optional<Income> updateIncome(String id, Income input) {
        Optional<Income> found = getIncomeById(id);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        if (accountService.getAccountById(input.getAccountId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        Income existing = found.get();

        // 1. take the old income back out
        accountService.adjustBalance(existing.getAccountId(), existing.getAmount().negate());
        // 2. add the new income
        accountService.adjustBalance(input.getAccountId(), input.getAmount());

        existing.setDescription(input.getDescription());
        existing.setAmount(input.getAmount());
        existing.setCategory(input.getCategory());
        existing.setAccountId(input.getAccountId());
        existing.setDate(input.getDate());
        return Optional.of(existing);
    }

    public boolean deleteIncome(String id) {
        Optional<Income> found = getIncomeById(id);
        if (found.isEmpty()) {
            return false;
        }
        Income income = found.get();

        // deleting an income takes the money back out
        accountService.adjustBalance(income.getAccountId(), income.getAmount().negate());
        incomes.remove(income);
        return true;
    }
}