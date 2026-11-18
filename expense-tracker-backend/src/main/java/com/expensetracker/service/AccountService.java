package com.expensetracker.service;

import com.expensetracker.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final List<Account> accounts = new ArrayList<>();

    public AccountService() {
        addAccount(new Account(null, "Cash", new BigDecimal("1000.00")));
        addAccount(new Account(null, "GCash", new BigDecimal("500.00")));
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

    public Optional<Account> getAccountById(String id) {
        return accounts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public Account addAccount(Account input) {
        if (nameExists(input.getName(), null)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account name already exists");
        }
        Account saved = new Account(UUID.randomUUID().toString(),
                input.getName(), input.getBalance());
        accounts.add(saved);
        return saved;
    }

    public Optional<Account> updateAccount(String id, Account input) {
        Optional<Account> found = getAccountById(id);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        if (nameExists(input.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account name already exists");
        }
        Account existing = found.get();
        existing.setName(input.getName());
        existing.setBalance(input.getBalance());
        return Optional.of(existing);
    }

    public boolean deleteAccount(String id) {
        return accounts.removeIf(a -> a.getId().equals(id));
    }

    // change can be positive (add money) or negative (remove money)
    // returns false only if the account doesn't exist
    public boolean adjustBalance(String accountId, BigDecimal change) {
        Optional<Account> found = getAccountById(accountId);
        if (found.isEmpty()) {
            return false;
        }
        Account account = found.get();
        account.setBalance(account.getBalance().add(change));
        return true;
    }

    private boolean nameExists(String name, String excludeId) {
        for (Account account : accounts) {
            boolean sameName = account.getName().equalsIgnoreCase(name);
            boolean differentAccount = !account.getId().equals(excludeId);
            if (sameName && differentAccount) {
                return true;
            }
        }
        return false;
    }
}