import java.util.Scanner;


enum Coin {ONE,TWO,FIVE,TEN};


public class VendingMachine {
    public static void main(String args[])
    {
        Scanner sc= new Scanner(System.in);
        int price=20;
        int total=0;
        while(price>total)
        {
            System.out.print("Enter coin:");
            String input =sc.next().toUpperCase();
            Coin coin=Coin.valueOf(input);
            int value= switch(coin)
            {
                case ONE->1;
                case TWO->2;
                case FIVE->5;
                case TEN->10;
                default ->0;
            };
            total+=value;
            System.out.println("Total paid= "+ total);
        }
        if(total>price)
        {
                
                 System.out.print("Paid Change:"+ (total-price));
        }
    }
}
