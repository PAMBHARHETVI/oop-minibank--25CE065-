package service;

import model.Account;

public final class StatementFormatter {
    private StatementFormatter() {
    }

    public static String buildStatement(Account account) {
        if (account == null)
            throw new IllegalArgumentException("Account cannot be null.");
        return new StringBuilder()
                .append("========== ACCOUNT STATEMENT ==========\n")
                .append("Account holder : ").append(account.getOwnerName()).append('\n')
                .append("Account number : ").append(account.getAccountNumber()).append('\n')
                .append("Balance        : ₹").append(account.getBalance()).append('\n')
                .append("Interest rate  : ").append(account.interestRate()).append("%\n")
                .append("Account status : ").append(account.isActive() ? "Active" : "Inactive").append('\n')
                .append("=======================================").toString();
    }
}
