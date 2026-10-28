import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.BankInfo;
import static model.BankConstants.BANK_NAME;
import static model.BankConstants.DEFAULT_BRANCH;
import model.Command;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import model.TransactionType;
import model.WithdrawRule;
import service.AccountStore;
import service.AccountWorker;
import service.ProducerConsumerDemo;
import service.BankSession;
import service.CommandParser;
import service.ReportGenerator;
import service.Repository;
import service.StatePersister;
import service.StatementFormatter;
import service.TransactionLog;
import service.TransactionProcessor;
import util.Validator;
import util.AnnotationValidator;

public class MiniBank {
    private static final AccountStore store = new AccountStore();
    private static final Repository<Account> repository = new Repository<>();
    private static final Path DATA_FILE = Path.of("data", "accounts.ser");
    private static final Path LOG_FILE = Path.of("logs", "transactions.log");
    private static long customerSequence = 0;

    public static void main(String[] args) {
        if (args.length > 0 && "--demo".equals(args[0])) {
            try {
                demo();
            } catch (Exception e) {
                System.err.println("Demo failed: " + e.getMessage());
            }
            return;
        }
        System.out.println("==================================================");
        System.out.println(new BankInfo(BANK_NAME, DEFAULT_BRANCH));
        System.out.println("Secure console banking | Amounts are whole rupees");
        System.out.println("==================================================");
        try (Scanner scanner = new Scanner(System.in)) {
            loadSavedAccounts();
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Choose an option: ");
                String raw = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(raw);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number from 1 to 8.");
                    continue;
                }
                MenuOption option = switch (choice) {
                    case 1 -> MenuOption.OPEN_ACCOUNT;
                    case 2 -> MenuOption.DEPOSIT;
                    case 3 -> MenuOption.WITHDRAW;
                    case 4 -> MenuOption.TRANSFER;
                    case 5 -> MenuOption.VIEW_ACCOUNTS;
                    case 6 -> MenuOption.STATEMENT;
                    case 7 -> MenuOption.SAVE_AND_REPORT;
                    case 8 -> MenuOption.EXIT;
                    default -> null;
                };
                if (option == null) {
                    System.out.println("Invalid choice.");
                    continue;
                }
                try {
                    switch (option) {
                        case OPEN_ACCOUNT -> openAccount(scanner);
                        case DEPOSIT -> deposit(scanner);
                        case WITHDRAW -> withdraw(scanner);
                        case TRANSFER -> transfer(scanner);
                        case VIEW_ACCOUNTS -> listAccounts();
                        case STATEMENT -> statement(scanner);
                        case SAVE_AND_REPORT -> saveAndReport();
                        case EXIT -> {
                            saveAccounts();
                            System.out.println("Thank you for using MiniBank. Goodbye!");
                            running = false;
                        }
                    }
                } catch (BankException e) {
                    System.out.println("Banking operation failed: " + e.getMessage());
                    if (e instanceof InsufficientFundsException funds)
                        System.out.println("Shortfall: ₹" + funds.getShortfall());
                } catch (IllegalArgumentException | IOException e) {
                    System.out.println("Operation failed: " + e.getMessage());
                } finally {
                    // A finally block runs after every selected menu action.
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n---------------- MiniBank Menu ----------------");
        System.out.println("1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. List accounts");
        System.out.println("6. Print account statement");
        System.out.println("7. Save accounts and generate report");
        System.out.println("8. Exit");
    }

    private static void openAccount(Scanner scanner) throws IOException {
        System.out.print("Account holder name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        System.out.print("Type (1=Savings, 2=Current, 3=Fixed Deposit): ");
        int type = readInt(scanner);
        System.out.print("Opening balance (rupees): ");
        long balance = readLong(scanner);
        Account account;
        switch (type) {
            case 1 -> {
                System.out.print("Minimum balance: ");
                long minimum = readLong(scanner);
                account = new SavingsAccount(name, balance, minimum);
            }
            case 2 -> {
                System.out.print("Overdraft limit: ");
                long limit = readLong(scanner);
                account = new CurrentAccount(name, balance, limit);
            }
            case 3 -> account = new FixedDepositAccount(name, balance);
            default -> throw new IllegalArgumentException("Unknown account type.");
        }
        store.add(account);
        repository.save(account);
        store.addTransaction("OPEN " + account.getAccountNumber() + " " + balance);
        TransactionLog.append(LOG_FILE, "OPEN " + account.getAccountNumber() + " " + balance);
        System.out.println("Account created: " + account);
    }

    private static void deposit(Scanner scanner) throws BankException, IOException {
        Account account = requireAccount(scanner, "Account number: ");
        System.out.print("Amount to deposit: ");
        long amount = readLong(scanner);
        account.deposit(amount);
        log("DEPOSIT", account, amount);
        System.out.println("Deposit successful. New balance: ₹" + account.getBalance());
    }

    private static void withdraw(Scanner scanner) throws BankException, IOException {
        Account account = requireAccount(scanner, "Account number: ");
        System.out.print("Amount to withdraw: ");
        long amount = readLong(scanner);
        account.withdraw(amount);
        log("WITHDRAW", account, amount);
        System.out.println("Withdrawal successful. New balance: ₹" + account.getBalance());
    }

    private static void transfer(Scanner scanner) throws BankException, IOException {
        Account from = requireAccount(scanner, "From account number: ");
        Account to = requireAccount(scanner, "To account number: ");
        System.out.print("Amount to transfer: ");
        long amount = readLong(scanner);
        from.transfer(to, amount);
        log("TRANSFER", from, amount);
        TransactionLog.append(LOG_FILE, "TRANSFER_TO " + to.getAccountNumber() + " " + amount);
    }

    private static Account requireAccount(Scanner scanner, String prompt) throws exception.AccountNotFoundException {
        System.out.print(prompt);
        String number = scanner.nextLine().trim();
        Account account = store.get(number);
        if (account == null)
            throw new exception.AccountNotFoundException("No account found with number " + number);
        return account;
    }

    private static void listAccounts() {
        ArrayList<Account> accounts = store.sortedByBalance();
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        System.out.println("Accounts (sorted by balance):");
        for (Account account : accounts)
            System.out.println(account + " | interest=" + account.interestRate() + "% | yearly estimate=₹"
                    + account.yearlyInterest());
    }

    private static void statement(Scanner scanner) throws exception.AccountNotFoundException {
        Account account = requireAccount(scanner, "Account number: ");
        System.out.println(StatementFormatter.buildStatement(account));
    }

    private static void saveAndReport() throws IOException {
        saveAccounts();
        System.out.println(ReportGenerator.generate(Path.of("logs"), Path.of("reports", "end-of-day.txt")));
    }

    private static void saveAccounts() throws IOException {
        StatePersister.save(store.getAll().toArray(Account[]::new), DATA_FILE);
    }

    private static void loadSavedAccounts() {
        if (!java.nio.file.Files.exists(DATA_FILE))
            return;
        try {
            for (Account account : StatePersister.load(DATA_FILE)) {
                store.add(account);
                repository.save(account);
            }
            System.out.println("Loaded " + store.getAll().size() + " saved account(s).");
        } catch (IOException | ClassNotFoundException | RuntimeException e) {
            System.out.println("Could not load saved accounts: " + e.getMessage());
        }
    }

    private static void log(String type, Account account, long amount) throws IOException {
        String line = type + " " + account.getAccountNumber() + " " + amount;
        store.addTransaction(line);
        TransactionLog.append(LOG_FILE, line);
    }

    private static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid whole number.");
        }
    }

    private static long readLong(Scanner scanner) {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid whole-rupee amount.");
        }
    }

    /**
     * Run practical demonstrations separately with: java -jar MiniBank.jar --demo
     */
    private static void demo() throws Exception {
        Account a = new SavingsAccount("Demo A", 5000, 500);
        Account b = new CurrentAccount("Demo B", 2000, 1000);
        System.out.println(a);
        System.out.println(b);
        WithdrawRule anonymous = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return amount > 0 && amount <= account.getBalance();
            }
        };
        WithdrawRule lambda = (account, amount) -> amount > 0 && amount <= account.getBalance();
        System.out.println("Anonymous withdraw rule: " + anonymous.allow(a, 100));
        System.out.println("Lambda withdraw rule: " + lambda.allow(a, 100));
        System.out.println("Valid mobile: " + Validator.isValidMobile("9876543210"));
        Command command = CommandParser.parse("DEPOSIT " + a.getAccountNumber() + " 100");
        System.out.println("Parsed command: " + command);
        for (String error : AnnotationValidator.validate(new SavingsAccount("Demo C", 100, 0)))
            System.out.println(error);
        try (BankSession session = new BankSession()) {
            session.performTask();
        }
        long beforeConcurrent = a.getBalance();
        Thread[] workers = new Thread[10];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(new AccountWorker(a, 100, 10), "BankWorker-" + (i + 1));
            workers[i].start();
        }
        for (Thread worker : workers)
            worker.join();
        System.out.println("Concurrent deposit balance: " + a.getBalance()
                + " (expected " + (beforeConcurrent + 10_000) + ")");
        try (TransactionProcessor processor = new TransactionProcessor()) {
            processor.register(a);
            processor.submit(() -> System.out.println("Managed pool task completed for " + a.getAccountNumber()));
        }
        new ProducerConsumerDemo().runDemo();
        Path demoState = Path.of("data", "demo-accounts.ser");
        StatePersister.save(new Account[] { a, b }, demoState);
        System.out.println("Reloaded accounts: " + StatePersister.load(demoState).length);
        Repository<Account> repo = new Repository<>();
        repo.saveAll(a, b);
        System.out.println("Repository count: " + repo.findAll().size());
        System.out.println("Average: " + Repository.average(new Integer[] { 1, 2, 3 }));
    }
}
