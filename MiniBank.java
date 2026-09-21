import java.util.Scanner;

public class MiniBank{

    record BankInfo(String name, String branch){}

    record Command(TransactionType type, String accountNumber, long amount) {}

    enum MenuOption{OPEN_ACCOUNT, DEPOSITE, WITHDRAW, TRANSFER, EXIT}

    enum TransactionType {DEPOSIT, WITHDRAW, TRANSFER }
    public static void main(String args[])
    {
        Scanner sc= new Scanner(System.in);

        //Bank Information (Practical 1)

        BankInfo bank = new BankInfo("Minibank" , "CHARUSAT Branch");

        System.out.println("================================");
        System.out.println("          " + bank.name());
        System.out.println("          " + bank.branch());
        System.out.println("================================");

        //Customer and Account Testing (Practical 2)

        Customer customer1 = new Customer(
                "Hetvi",
                "hetvi@gmail.com",
                "9876543210"
        );

        Customer.Address address = new Customer.Address(
                "CHARUSAT Road",
                "Anand",
                "388421"
        );

        customer1.setAddress(address);

        System.out.println("\nCustomer Information:");
        System.out.println("Customer ID : " + customer1.getCustomerId());
        System.out.println("Name        : " + customer1.getName());
        System.out.println("Email       : " + customer1.getEmail());
        System.out.println("Mobile      : " + customer1.getMobile());
        System.out.println("City        : " + customer1.getAddress().getCity());
        
        // Account Testing (Practical 3)

        account[] accounts = {
            new SavingsAccount("Hetvi" ,10000, 5000),
            new CurrentAccount("Satyam" , 5000,3000),
            new FixedDepositAccount("Dimpal",20000)
        };

        //deposit and withdraw

        accounts[0].deposit(20000);
        accounts[1].deposit(50000);
        accounts[2].deposit(7000);

        accounts[0].Withdraw(5000);
        accounts[1].Withdraw(800);

        System.out.println("----- Account Details -----");

        for (account acc : accounts) {
           
            System.out.println(acc);

            System.out.println("InterrestRate() : " + acc.interestRate() + "%");

            System.out.println();
        }

        System.out.println("---- Withdrawal Testing ---");

        boolean savingResult = accounts[0].Withdraw(5000);

        System.out.println("Saving Account withdrawal : " + savingResult);

        boolean currentResult = accounts[1].Withdraw(800);

        System.out.println("Current Account withdraw : " + currentResult);

        boolean FixedDepositResult = accounts[2].Withdraw(1000);

        System.out.println("Fixed Deposite withdrawal : " + FixedDepositResult);

        System.out.println("\n---- Updated Account Details ----");

        for(account acc : accounts)
        {
            System.out.println(acc);
        }

        System.out.println("\n---- equals() Testing ----");

        account a1 = new SavingsAccount("Riya" , 4000,1000);
        account a2 = new SavingsAccount("Riya" , 4000,1000);

        System.out.println("Account 1 :" + a1);
        System.out.println("Account 2 :" + a2);

        System.out.println("a1.equals(a2) : " + a1.equals(a2));

        System.out.println("a1.hashCode() : " + a1.hashCode());
        System.out.println("a2.hashCode() : " + a2.hashCode());

        System.out.println("\n----- Customer Details -----");

         Customer customer = new Customer(
                "Riya",
                "riya@gmail.com",
                "9876543210"
        );

        Customer.Address customerAddress = new Customer.Address(
                "123 Main Road",
                "Vadodara",
                "390001"
        );

        customer.setAddress(customerAddress);

        System.out.println("Customer ID : " + customer.getCustomerId());
        System.out.println("Name        : " + customer.getName());
        System.out.println("Email       : " + customer.getEmail());
        System.out.println("Mobile      : " + customer.getMobile());
        System.out.println("Address     : " + customer.getAddress());

        System.out.println("\n----- Clone Testing -----");

        Customer clonedCustomer = customer.clone();

        System.out.println("Original Customer : " + customer.getName());
        System.out.println("Cloned Customer   : " + clonedCustomer.getName());
        System.out.println("Clone successful  : " + (customer != clonedCustomer));

        System.out.println("\n----- instanceof Testing -----");

        for(account acc : accounts)
        {
            if(acc instanceof SavingsAccount)
            {
                System.out.println(acc.getOwnerName() + " is a Savings Account");
            }

            if(acc instanceof CurrentAccount)
            {
                System.out.println(acc.getOwnerName() + " is a Current Account");
            }

            if(acc instanceof FixedDepositAccount)
            {
                System.out.println(acc.getOwnerName() + " is a Fixed Deposite Account");
            }
        }
        
        if (customer instanceof Customer) {

            System.out.println("customer is a Customer");
        }

        //Validator Testing

        System.out.println("\nValidator Tests:");

        System.out.println(
                "Valid Mobile: "
                        + Validator.isValidMobile("9876543210")
        );

        System.out.println(
                "Invalid Mobile: "
                        + Validator.isValidMobile("12345")
        );

        System.out.println(
                "Valid Email: "
                        + Validator.isValidEmail("hetvi@gmail.com")
        );

        System.out.println(
                "Invalid Email: "
                        + Validator.isValidEmail("hetvi@")
        );

        System.out.println(
                "Valid PAN: "
                        + Validator.isValidPan("ABCDE1234F")
        );

        System.out.println(
                "Invalid PAN: "
                        + Validator.isValidPan("ABC123")
        );

        System.out.println(
                "Valid IFSC: "
                        + Validator.isValidIfsc("SBIN0001234")
        );

        System.out.println(
                "Invalid IFSC: "
                        + Validator.isValidIfsc("SBIN123")
        );

        // CommandParser Testing

        System.out.println("\nCommand Parser Test:");

        String input = "DEPOSIT AC0001 500";

        Command command = CommandParser.parse(input);

        System.out.println("Command Type    : " + command.type());
        System.out.println("Account Number  : " + command.accountNumber());
        System.out.println("Amount          : " + command.amount());


        // StatementFormatter Testing

        System.out.println("\nAccount Statement:");

        System.out.println(
                StatementFormatter.buildStatement(accounts[0])
        );

        String choice="null";

        while(!choice.equals("EXIT"))
        {
            
            System.out.println("1. Open_Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.println();

            System.out.print("Enter your choice: ");
            choice = sc.next().toUpperCase();
            
            MenuOption menuoption=MenuOption.valueOf(choice);

            switch(menuoption){

                case OPEN_ACCOUNT:
                    System.out.println("Opening account - to be implemented in a later lab.");
                    break;
                case DEPOSITE:
                    System.out.println("Deposite - to be implemented in a later lab.");
                    break;
                case WITHDRAW:
                    System.out.println("Withdraw - to be implemented in a later lab.");
                    break;
                case TRANSFER:
                    System.out.println("Transfer - to be implemented in a later lab.");
                    break;
                case EXIT:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}