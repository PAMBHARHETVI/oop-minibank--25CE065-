import java.util.Scanner;

record BankInfo(String name, String branch){}

enum MenuOption{OPEN_ACCOUNT, DEPOSITE, WITHDRAW, TRANSFER, EXIT}

public class MiniBank{
    public static void main(String args[])
    {
        Scanner sc= new Scanner(System.in);

        account[] accounts = {
            new account("Hetvi" , 5000),
            new account("satyam" , 3000),
            new account("Dimpal")
        };

        accounts[0].deposite(20000);
        accounts[1].deposite(50000);
        accounts[2].deposite(7000);

        accounts[0].Withdraw(5000);
        accounts[1].Withdraw(800);

        System.out.println("----- Account Details -----");

        for (account acc : accounts) {
            System.out.println("Account Number : " + acc.getAccountNumber());
            System.out.println("Owner Name     : " + acc.getOwnerName());
            System.out.println("Balance        : " + acc.getBalance());
            System.out.println("Active         : " + acc.isActive());
            System.out.println();
        }
        String choice="null";
        while(!choice.equals("EXIT"))
        {
            
            System.out.println("1. Open_Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
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