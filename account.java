class account {
    
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    account(String ownerName, long opening_balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = opening_balance;
        this.active = true;
    }

    account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposite(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean Withdraw(long amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }

        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}