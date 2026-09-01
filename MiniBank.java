import java.util.Scanner;

record BankInfo(String name, String branch){}

enum MenuOption{OPEN_ACCOUNT, DEPOSITE, WITHDRAW, TRANSFER, EXIT}

public class MiniBank{
    public static void main(String args[])
    {
        Scanner sc= new Scanner(System.in);

        BankInfo bank = new BankInfo("Minibank" , "CHARUSAT Branch");

        System.out.println("================================");
        System.out.println("          " + bank.name());
        System.out.println("          " + bank.branch());
        System.out.println("================================");

        account[] accounts = {
            new SavingsAccount("Hetvi" ,10000, 5000),
            new CurrentAccount("satyam" , 5000,3000),
            new FixedDepositAccount("Dimpal",20000)
        };

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

        System.out.println("\n---- equals() Testing ----");

        account a1 = new account("Riya" , 4000);
        account a2 = new account("Riya" , 4000);

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

        Customer.Address address = new Customer.Address(
                "123 Main Road",
                "Vadodara",
                "390001"
        );

        customer.setAddress(address);

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

        if (a1 instanceof account) {

            System.out.println("a1 is an Account");
        }

        if (customer instanceof Customer) {

            System.out.println("customer is a Customer");
        }

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