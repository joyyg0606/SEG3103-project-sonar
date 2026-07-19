package com.bankaccount;

/**
 * Thrown when an operation refers to an account ID that does not
 * exist in the bank.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }
}
