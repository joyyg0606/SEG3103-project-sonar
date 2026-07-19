package com.bankaccount;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages a collection of BankAccount objects and exposes the four
 * core operations used throughout the demo and the automated tests:
 * open an account, deposit, withdraw, check balance, and transfer
 * between two accounts.
 */
public class Bank {

    private final Map<String, BankAccount> accounts = new LinkedHashMap<>();

    /**
     * Opens a new account and adds it to the bank.
     */
    public BankAccount openAccount(String accountId, String ownerName, BigDecimal openingBalance) {
        if (accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account " + accountId + " already exists.");
        }
        BankAccount account = new BankAccount(accountId, ownerName, openingBalance);
        accounts.put(accountId, account);
        return account;
    }

    /**
     * Deposits money into the given account.
     */
    public void deposit(String accountId, BigDecimal amount) {
        getAccountOrThrow(accountId).deposit(amount);
    }

    /**
     * Withdraws money from the given account.
     */
    public void withdraw(String accountId, BigDecimal amount) {
        getAccountOrThrow(accountId).withdraw(amount);
    }

    /**
     * Returns the current balance of the given account.
     */
    public BigDecimal getBalance(String accountId) {
        return getAccountOrThrow(accountId).getBalance();
    }

    /**
     * Transfers money from one account to another. The withdrawal is
     * validated and applied first, so a failed deposit step never
     * leaves money "missing" from the source account.
     */
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }
        BankAccount from = getAccountOrThrow(fromAccountId);
        BankAccount to = getAccountOrThrow(toAccountId);

        from.withdraw(amount);
        to.deposit(amount);
    }

    public Collection<BankAccount> getAllAccounts() {
        return accounts.values();
    }

    private BankAccount getAccountOrThrow(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account " + accountId + " does not exist.");
        }
        return account;
    }
}
