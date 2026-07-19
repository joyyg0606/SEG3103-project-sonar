package com.bankaccount;

/**
 * Thrown when a deposit, withdrawal, or transfer amount is zero,
 * negative, or otherwise not a valid monetary amount.
 */
public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
