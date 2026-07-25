import java.util.Scanner;

record BankInfo(String name, String branch){}

enum MenuOption{OPEN_ACCOUNT, DEPOSITE, WITHDRAW, TRANSFER, EXIT}

public class MiniBank{
    public static void main(String args[])
    {
        Scanner sc= new Scanner(System.in);


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