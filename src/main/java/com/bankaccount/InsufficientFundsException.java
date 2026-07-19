package com.bankaccount;

/**
 * Thrown when a withdrawal or transfer is attempted for more money
 * than is currently available in the account.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
