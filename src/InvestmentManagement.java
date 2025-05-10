import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InvestmentManagement {
    static Scanner scanner = new Scanner(System.in);
    static Goal_Manager goalManager = new Goal_Manager();  // Instantiate the Goal Manager

    public static void investMentManagement() {
        Investment investment = new Investment();
        Portfolio portfolio = new Portfolio();

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add Asset");
            System.out.println("2. Edit Asset");
            System.out.println("3. Remove Asset");
            System.out.println("4. Show Portfolio");
            System.out.println("5. Add Goal");
            System.out.println("6. Display Goals");
            System.out.println("7. Track Progress of Goals");
            System.out.println("8. Remove Goal");
            System.out.println("9. Show Goal Details");
            System.out.println("10. calculate zakat");
            System.out.println("11. Connect Bank Account");
            System.out.println("12. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addAsset(investment);
                    portfolio.setMyInvestment(investment);
                    break;
                case "2":
                    editAsset(investment);
                    break;
                case "3":
                    removeAsset(investment);
                    break;
                case "4":
                    showPortfolio(investment);
                    break;
                case "5":
                    addGoal();
                    break;
                case "6":
                    displayGoals();
                    break;
                case "7":
                    trackGoalProgress();
                    break;
                case "8":
                    removeGoal();
                    break;
                case "9":
                    showGoalDetails();
                    break;

                case "10":
                    ALZakat(portfolio);
                    break;
                case "11":
                    connectBank();
                    break;
                case "12":
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Show Goal Details
    public static void showGoalDetails() {
        int goalCount = goalManager.getGoalCount();
        if (goalCount == 0) {
            System.out.println("No goals available.");
            return;
        }

        System.out.println("Select a goal to view details (1 to " + goalCount + "): ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < goalCount) {
            Goal goal = goalManager.getGoal(index);
            System.out.println("Goal Type: " + goal.type);
            System.out.println("Goal Amount: $" + goal.amount);
            System.out.println("Current Progress: $" + goal.currrentProgress);
            System.out.println("Remaining Amount: $" + goal.getRemainingAmount());
            System.out.println("Completion Percentage: " + goal.getCompletionPercentage() * 100 + "%");
            System.out.println("Is the Goal Achieved? " + (goal.IsAchieved() ? "Yes" : "No"));
            System.out.println("Is the Goal Overdue? " + (goal.isOverdue() ? "Yes" : "No"));
        } else {
            System.out.println("Invalid goal index. Please try again.");
        }
    }

    // Add Goal to the Goal Manager
    public static void addGoal() {
        System.out.println("\nAdding a new Goal:");
        double amount = promptDouble("Goal Amount");
        String type = prompt("Goal Type");
        LocalDate deadline = promptDate("Goal Deadline (yyyy-mm-dd)");
        double currentProgress = promptDouble("Current Progress");
        double autoSaveAmount = promptDouble("Auto Save Amount");
        boolean enableAutoSave = selectBoolean("Enable Auto Save?");

        Goal goal = new Goal(amount, type, deadline, currentProgress, autoSaveAmount, enableAutoSave);
        goalManager.addGoal(goal);
        System.out.println("Goal added successfully!");
    }

    // Display Goals
    public static void displayGoals() {
        System.out.println("\nDisplaying all Goals:");
        goalManager.displayGoals();
    }

    // Track Progress of Goals
    public static void trackGoalProgress() {
        goalManager.trackProgress();
        System.out.println("Progress of goals updated!");
    }

    // Remove Goal from Goal Manager
    public static void removeGoal() {
        int goalCount = goalManager.getGoalCount();
        if (goalCount == 0) {
            System.out.println("No goals to remove.");
            return;
        }

        System.out.println("Select the goal number to remove (1 to " + goalCount + "): ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < goalCount) {
            goalManager.removeGoal(index);
            System.out.println("Goal removed successfully!");
        } else {
            System.out.println("Invalid goal index. Please try again.");
        }
    }

    // Add Asset to investment
    public static void addAsset(Investment investment) {
        System.out.println("\nAdding a new Asset:");
        Asset newAsset = createAsset("new");
        investment.addAsset(newAsset);
        System.out.println("Asset added successfully!");
    }


    // Edit Asset in investment
    public static void editAsset(Investment investment) {
        System.out.println("\nEditing an Asset:");
        Asset oldAsset = createAsset("old");
        Asset newAsset = createAsset("new");
        investment.editAsset(oldAsset, newAsset);
    }

    // Remove Asset from investment
    public static void removeAsset(Investment investment) {
        System.out.println("\nRemoving an Asset:");
        Asset assetToRemove = createAsset("remove");
        investment.removeAsset(assetToRemove);
        System.out.println("Asset removed successfully!");
    }

    // Show investment Details
    public static void showPortfolio(Investment investment) {
        System.out.println("\nInvestment Details:");
        investment.listAssets();
        System.out.printf("\nTotal Investment Value: $%.2f%n", investment.getCurrentValue());
        System.out.printf("Investment Risk Score: %.2f%n", investment.calculateRiskScore());
        System.out.printf("ROI: %.2f%%%n", investment.recordROI());
        investment.displayAssetTypes();
        System.out.println("\nAsset Distribution:");
        investment.recordAssetDistribution().forEach((type, percent) -> {
            System.out.printf("- %s: %.2f%%%n", type, percent);
        });
    }

    // Helper Method to Create Asset
    static Asset createAsset(String assetType) {
        System.out.println("Enter details for the " + assetType + " asset:");
        String name = prompt("Name");
        double currentValue = promptDouble("Current Value");
        Asset_Type type = selectAssetType();
        boolean isActive = selectBoolean("Is Active? (1- Yes, 2- No)");
        LocalDate purchaseDate = promptDate("Purchase Date (yyyy-mm-dd)");
        double purchasePrice = promptDouble("Purchase Price");
        Risk_Cat risk = selectRiskCategory();

        return new Asset(name, currentValue, type, isActive, purchaseDate, purchasePrice, risk);
    }

    // Helper Method to Prompt User for Strings
    static String prompt(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }

    // Helper Method to Prompt User for Doubles
    static double promptDouble(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(" Invalid number. Try again.");
            }
        }
    }

    // Helper Method to Select Boolean Values
    static boolean selectBoolean(String label) {
        while (true) {
            System.out.print(label + " (1 or 2): ");
            String input = scanner.nextLine();
            if (input.equals("1")) return true;
            if (input.equals("2")) return false;
            System.out.println(" Invalid choice. Enter 1 or 2.");
        }
    }

    // Helper Method to Select Asset Type
    static Asset_Type selectAssetType() {
        System.out.println("Select Asset Type:");
        System.out.println("1. Stocks\n2. Bond\n3. RealState\n4. Gold");
        while (true) {
            System.out.print("Enter choice (1-4): ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    return Asset_Type.Stocks;
                case "2":
                    return Asset_Type.Bond;
                case "3":
                    return Asset_Type.RealState;
                case "4":
                    return Asset_Type.Gold;
                default:
                    System.out.println(" Invalid choice. Try again.");
            }
        }
    }

    // Helper Method to Select Risk Category
    static Risk_Cat selectRiskCategory() {
        System.out.println("Select Risk Category:");
        System.out.println("1. LOW\n2. MEDIUM\n3. HIGH\n4. EXTREME");
        while (true) {
            System.out.print("Enter choice (1-4): ");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    return Risk_Cat.Low;
                case "2":
                    return Risk_Cat.Medium;
                case "3":
                    return Risk_Cat.High;
                case "4":
                    return Risk_Cat.very_high;
                default:
                    System.out.println(" Invalid choice. Try again.");
            }
        }
    }

    // Helper Method to Prompt for Date Input
    static LocalDate promptDate(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println(" Invalid date. Please use format yyyy-mm-dd.");
            }
        }
    }

    public static void ALZakat(Portfolio portfolio){
        if (portfolio == null || portfolio.getLiquidAssets().isEmpty()) {
            System.out.println("No investments available for Zakat calculation!");
            return;
        }
        System.out.println("Enter zakat rate (e.g., 0.025 for 2.5%): ");
        double rate = Double.parseDouble(scanner.nextLine());
        Zakat_Calc zakatCalc = new Zakat_Calc(rate);
        zakatCalc.calculateZakat(portfolio);

    }
    public static void connectBank(){
        Bank_Connector bank = new Bank_Connector();
        System.out.println("Enter bank name");
        String bankName = scanner.nextLine();
        bank.selectBank(bankName);
        System.out.println("Enter your credit card number");
        String CardNo = scanner.nextLine();
        System.out.println("Enter card expiry (mm/yyyy)");
        String Expiry = scanner.nextLine();
        bank.inputAccountDetails(CardNo ,Expiry);
        bank.isConnected();
    }
}
