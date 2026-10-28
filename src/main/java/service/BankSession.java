package service;

public class BankSession implements AutoCloseable {
    public BankSession() {
        System.out.println("Bank session opened.");
    }

    public void performTask() {
        System.out.println("Bank session task completed.");
    }

    @Override
    public void close() {
        System.out.println("Bank session closed automatically.");
    }
}
