package com.bankaccount;

import java.math.BigDecimal;

/**
 * Simple runnable demo of the Bank Account System. Not used by the
 * automated tests — this is just for showing the codebase live
 * during the introduction section of the presentation.
 */
public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();

        bank.openAccount("A100", "Amina", new BigDecimal("500.00"));
        bank.openAccount("A200", "Marcus", new BigDecimal("150.00"));

        bank.deposit("A100", new BigDecimal("100.00"));
        bank.withdraw("A200", new BigDecimal("50.00"));
        bank.transfer("A100", "A200", new BigDecimal("200.00"));

        System.out.println("A100 balance: " + bank.getBalance("A100"));
        System.out.println("A200 balance: " + bank.getBalance("A200"));
    }
}
