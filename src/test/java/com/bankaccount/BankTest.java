package com.bankaccount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Bank Account System")
@DisplayName("Bank Account System Tests")
class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();

        bank.openAccount(
                "A100",
                "Amina",
                new BigDecimal("500.00")
        );

        bank.openAccount(
                "A200",
                "Marcus",
                new BigDecimal("150.00")
        );
    }

    @Test
    @Feature("Account Management")
    @Story("Open a bank account")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Open an account with the correct balance")
    void openAccountCreatesAccountWithCorrectBalance() {
        Allure.step("Open account A300 with $300.00", () ->
                bank.openAccount(
                        "A300",
                        "Sofia",
                        new BigDecimal("300.00")
                )
        );

        Allure.step("Verify the opening balance", () ->
                assertAmountEquals(
                        "300.00",
                        bank.getBalance("A300")
                )
        );
    }

    @Test
    @Feature("Account Management")
    @Story("Prevent duplicate account IDs")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Duplicate account ID is rejected")
    void duplicateAccountIdIsRejected() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bank.openAccount(
                        "A100",
                        "Another Owner",
                        new BigDecimal("200.00")
                )
        );

        assertEquals(
                "Account A100 already exists.",
                exception.getMessage()
        );
    }

    @Test
    @Feature("Deposits")
    @Story("Deposit a valid amount")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Deposit increases the account balance")
    void depositIncreasesBalance() {
        Allure.step("Deposit $100.00 into account A100", () ->
                bank.deposit(
                        "A100",
                        new BigDecimal("100.00")
                )
        );

        Allure.step("Verify that the balance is $600.00", () ->
                assertAmountEquals(
                        "600.00",
                        bank.getBalance("A100")
                )
        );
    }

    @Test
    @Feature("Deposits")
    @Story("Reject zero deposits")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Zero deposit is rejected")
    void zeroDepositIsRejected() {
        assertThrows(
                InvalidAmountException.class,
                () -> bank.deposit(
                        "A100",
                        BigDecimal.ZERO
                )
        );

        assertAmountEquals(
                "500.00",
                bank.getBalance("A100")
        );
    }

    @Test
    @Feature("Deposits")
    @Story("Reject negative deposits")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Negative deposit is rejected")
    void negativeDepositIsRejected() {
        assertThrows(
                InvalidAmountException.class,
                () -> bank.deposit(
                        "A100",
                        new BigDecimal("-50.00")
                )
        );

        assertAmountEquals(
                "500.00",
                bank.getBalance("A100")
        );
    }

    @Test
    @Feature("Withdrawals")
    @Story("Withdraw a valid amount")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Withdrawal decreases the account balance")
    void withdrawalDecreasesBalance() {
        Allure.step("Withdraw $125.00 from account A100", () ->
                bank.withdraw(
                        "A100",
                        new BigDecimal("125.00")
                )
        );

        Allure.step("Verify the remaining balance", () ->
                assertAmountEquals(
                        "375.00",
                        bank.getBalance("A100")
                )
        );
    }

    @Test
    @Feature("Withdrawals")
    @Story("Prevent overdrafts")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Withdrawal above the balance is rejected")
    void withdrawalAboveBalanceIsRejected() {
        assertThrows(
                InsufficientFundsException.class,
                () -> bank.withdraw(
                        "A200",
                        new BigDecimal("200.00")
                )
        );

        assertAmountEquals(
                "150.00",
                bank.getBalance("A200")
        );
    }

    @Test
    @Feature("Transfers")
    @Story("Transfer a valid amount")
    @Severity(SeverityLevel.BLOCKER)
    @Description(
            "Verifies that a transfer decreases the sender's balance " +
            "and increases the receiver's balance."
    )
    @DisplayName("Transfer updates both account balances")
    void transferUpdatesBothBalances() {
        Allure.step("Transfer $200.00 from A100 to A200", () ->
                bank.transfer(
                        "A100",
                        "A200",
                        new BigDecimal("200.00")
                )
        );

        Allure.step("Verify both balances", () ->
                assertAll(
                        () -> assertAmountEquals(
                                "300.00",
                                bank.getBalance("A100")
                        ),
                        () -> assertAmountEquals(
                                "350.00",
                                bank.getBalance("A200")
                        )
                )
        );
    }

    @Test
    @Feature("Transfers")
    @Story("Prevent same-account transfers")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Transfer to the same account is rejected")
    void transferToSameAccountIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bank.transfer(
                        "A100",
                        "A100",
                        new BigDecimal("100.00")
                )
        );

        assertAmountEquals(
                "500.00",
                bank.getBalance("A100")
        );
    }

    @Test
    @Feature("Transfers")
    @Story("Prevent transfers with insufficient funds")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Failed transfer does not change either balance")
    void failedTransferDoesNotChangeBalances() {
        assertThrows(
                InsufficientFundsException.class,
                () -> bank.transfer(
                        "A200",
                        "A100",
                        new BigDecimal("500.00")
                )
        );

        assertAll(
                () -> assertAmountEquals(
                        "500.00",
                        bank.getBalance("A100")
                ),
                () -> assertAmountEquals(
                        "150.00",
                        bank.getBalance("A200")
                )
        );
    }

    @Test
    @Feature("Account Lookup")
    @Story("Access an unknown account")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Unknown account ID is rejected")
    void unknownAccountIdIsRejected() {
        assertThrows(
                AccountNotFoundException.class,
                () -> bank.getBalance("UNKNOWN")
        );
    }

    private void assertAmountEquals(
            String expected,
            BigDecimal actual
    ) {
        assertEquals(
                0,
                actual.compareTo(new BigDecimal(expected))
        );
    }
}