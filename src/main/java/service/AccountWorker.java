package service;

import model.Account;
import exception.InvalidAmountException;

public class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        if (times < 0)
            throw new IllegalArgumentException("times cannot be negative.");
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            try {
                account.deposit(amount);
            } catch (InvalidAmountException e) {
                System.err.println(Thread.currentThread().getName() + ": " + e.getMessage());
                return;
            }
        }
        System.out.println(Thread.currentThread().getName() + " finished.");
    }
}
