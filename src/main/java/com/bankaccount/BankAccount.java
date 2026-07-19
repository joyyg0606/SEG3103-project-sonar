package com.bankaccount;

import java.math.BigDecimal;

/**
 * Represents a single bank account with a unique ID, an owner name,
 * and a balance. Balances are stored as BigDecimal to avoid the
 * rounding errors that come with using double for money.
 */
public class BankAccount {

    private final String accountId;
    private final String ownerName;
    private BigDecimal balance;

    public BankAccount(String accountId, String ownerName, BigDecimal openingBalance) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be empty.");
        }
        if (openingBalance == null || openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = openingBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Adds money to the account. Amount must be strictly positive.
     */
    void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
    }

    /**
     * Removes money from the account. Amount must be strictly positive
     * and no larger than the current balance.
     */
    void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds in account " + accountId
                            + ". Balance: " + balance + ", requested: " + amount);
        }
        balance = balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }
}
