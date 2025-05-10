import java.util.Scanner;
import java.io.*;

public class mainMenu {
    private static final UserManager manager = new UserManager();
    private static final Scanner in = new Scanner(System.in);


    public mainMenu(){
        boolean run = true;
        while(run){
            System.out.println("---- Invest Wise ----");
            System.out.println("1. Register\n2. Login\n3. Exit");
            System.out.print("choose an option: ");

            int choice = in.nextInt();
            in.nextLine();


            switch(choice){
                case 1:
                    System.out.print("Enter name: ");
                    String name = in.next();
                    in.nextLine();
                    System.out.print("Enter email: ");
                    String email = in.next();
                    in.nextLine();
                    System.out.print("Enter username: ");
                    String username = in.next();
                    in.nextLine();
                    System.out.print("Enter password: ");
                    String pass = in.next();
                    in.nextLine();
                    try {
                        manager.registerUser(name, email, username, pass);
                        System.out.println("Registration successful!");
                        Investment investment = new Investment(username);
                        Investment portfolio = Investment.loadFromFile(username);
                        portfolio.setUserName(username);
                        InvestmentManagement.investMentManagement();
                        break;

                    } catch (IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String username_ = in.next();
                    in.nextLine();
                    System.out.print("Enter password: ");
                    String pass_ = in.next();
                    in.nextLine();
                    try {
                        boolean isAuthenticated = manager.loginUser(username_, pass_);
                        if (isAuthenticated) {
                            System.out.println("Login successful! Welcome, " + username_ + "!");
                            Investment investment = new Investment(username_);
                            Investment portfolio = Investment.loadFromFile(username_);
                            InvestmentManagement.investMentManagement();

                            break;
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    } catch (IOException e) {
                        System.err.println("Error: " + e.getMessage());
                        in.nextLine();
                    }
                    break;

                case 3:
                    System.out.println("Exiting...");
                    run = false;
                    break;

                default:
                    System.out.println("Invalid choice");

            }

        }


    }


}
