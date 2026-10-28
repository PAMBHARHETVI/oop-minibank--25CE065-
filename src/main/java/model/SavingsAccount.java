package model;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long minBalance;

    public SavingsAccount(String ownerName, long initialBalance, long minBalance) {
        super(ownerName, initialBalance);
        if (minBalance < 0)
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        this.minBalance = minBalance;
    }

    public SavingsAccount(String ownerName, long minBalance) {
        this(ownerName, 0, minBalance);
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && getBalance() - amount >= minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }
}
