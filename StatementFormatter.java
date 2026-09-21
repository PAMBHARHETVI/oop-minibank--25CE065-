public class StatementFormatter {

    public static String buildStatement(account Account) {

        StringBuilder statement = new StringBuilder();


        statement.append("========== ACCOUNT STATEMENT ==========\n");

        statement.append("Account Number : ")
                .append(Account.getAccountNumber())
                .append("\n");

        statement.append("Owner Name     : ")
                .append(Account.getOwnerName())
                .append("\n");

        statement.append("Balance        : ₹")
                .append(Account.getBalance())
                .append("\n");

        statement.append("Active         : ")
                .append(Account.isActive())
                .append("\n");

        statement.append("Interest Rate  : ")
                .append(Account.interestRate())
                .append("%\n");

        statement.append("========================================");


        return statement.toString();
    }
}