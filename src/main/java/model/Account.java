package model;

import java.io.Serializable;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;
    private static long accountCounter = 0;
    @Id
    private final String accountNumber;
    @MaxLength(100)
    private final String ownerName;
    @Positive(message = "must be > 0")
    private long balance;
    private boolean active = true;

    public static synchronized void reserveAccountNumber(String number) {
        if (number != null && number.matches("AC\\d{4,}")) {
            try {
                accountCounter = Math.max(accountCounter, Long.parseLong(number.substring(2)));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    protected Account(String ownerName, long initialBalance) {
        if (ownerName == null || ownerName.isBlank())
            throw new IllegalArgumentException("Owner name cannot be empty.");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        synchronized (Account.class) {
            this.accountNumber = String.format("AC%04d", ++accountCounter);
        }
        this.ownerName = ownerName;
        this.balance = initialBalance;
    }

    protected Account(String ownerName) {
        this(ownerName, 0);
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        try {
            balance = Math.addExact(balance, amount);
        } catch (ArithmeticException e) {
            throw new InvalidAmountException("Deposit would exceed the maximum supported balance.");
        }
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        if (!active)
            throw new InvalidAmountException("Account is inactive.");
        if (!canWithdraw(amount)) {
            long shortfall = Math.max(0L, amount - Math.max(0L, balance));
            throw new InsufficientFundsException("Withdrawal denied for account " + accountNumber, shortfall);
        }
        balance = Math.subtractExact(balance, amount);
        return true;
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null)
            throw new BankException("Destination account does not exist.");
        if (to == this)
            throw new BankException("Cannot transfer money to the same account.");
        Account first = accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = first == this ? to : this;
        synchronized (first) {
            synchronized (second) {
                try {
                    if (amount <= 0)
                        throw new InvalidAmountException("Transfer amount must be greater than zero.");
                    if (!canWithdraw(amount))
                        throw new InsufficientFundsException("Insufficient funds in account " + accountNumber,
                                Math.max(0L, amount - Math.max(0L, balance)));
                    long newDestination;
                    try {
                        newDestination = Math.addExact(to.balance, amount);
                    } catch (ArithmeticException e) {
                        throw new InvalidAmountException("Destination balance would overflow.");
                    }
                    withdraw(amount);
                    to.deposit(amount);
                    System.out.println(
                            "Transfer successful: " + amount + " from " + accountNumber + " to " + to.accountNumber);
                } finally {
                    System.out.println("Transfer operation completed.");
                }
            }
        }
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void deactivate() {
        active = false;
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: " + getBalance() + " | Active: " + active;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Account other && accountNumber.equals(other.accountNumber));
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}
