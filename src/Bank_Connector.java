import java.security.SecureRandom;
import java.util.Scanner;

public class Bank_Connector {
   private String bank_name;
   private String encCardNo;
   private String encExpiry;
   private boolean is_connected;

   public void selectBank(String bankName){
        bank_name = bankName;
   }

   public void inputAccountDetails(String CardNo ,String Expiry){
        encCardNo = CardNo;
        encExpiry = Expiry;
        Scanner scanner = new Scanner(System.in);
        String Cardno;
        String expiry;
        if (!encCardNo.matches("^(\\d{4}-?){3}\\d{4}$") ){
           System.out.println("Invalid Card Number!");
           System.out.println("Please enter credit card number again");
           Cardno = scanner.nextLine();
           inputAccountDetails(Cardno,Expiry);
        }
        if(!encExpiry.matches("^(0[1-9]|1[1-2])[- /](20\\d{2})$")){
           System.out.println("Invalid Expiry Date");
           System.out.println("Please enter expiry date again");
           expiry = scanner.nextLine();
           inputAccountDetails(CardNo,expiry);
        }


   }

   public boolean isConnected(){
      SecureRandom secureRandom = new SecureRandom();
      int OTP = secureRandom.nextInt(100000);
      String formattedOTP = String.format("%05d", OTP);

      System.out.println("\n-----------------------------------------");
      System.out.println("|               IMPORTANT!               |");
      System.out.println("-----------------------------------------");
      System.out.println("| Your One-Time Password (OTP) is: " + formattedOTP + " |");
      System.out.println("| This OTP is valid for a single use.   |");
      System.out.println("| Please enter it again to confirm.      |");
      System.out.println("-----------------------------------------");
      int t = 3;
      boolean flag = false;
      Scanner scanner = new Scanner(System.in);
      while (t > 0) {
         System.out.print("\nEnter the OTP you received: ");
         String input = scanner.nextLine();

         if (input.equals(formattedOTP)) {
            System.out.println("\nOTP verified successfully! Access granted.");
            flag = true;
            break;
         } else {
            t--;
            if (t == 0) {
               System.out.println("\nPlease request a new OTP after some time.");
            } else {
               System.out.println("\nInvalid OTP. Please try again.");
            }
         }
      }
      if (flag) return true;
      else return false;
   }
}
