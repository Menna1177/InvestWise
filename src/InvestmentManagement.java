import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InvestmentManagement {
    static Scanner scanner = new Scanner(System.in);
    static Goal_Manager goalManager = new Goal_Manager();  // Instantiate the Goal Manager

    public static void investMentManagement() {
        Investment portfolio = new Investment();

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
            System.out.println("10. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addAsset(portfolio);
                    break;
                case "2":
                    editAsset(portfolio);
                    break;
                case "3":
                    removeAsset(portfolio);
                    break;
                case "4":
                    showPortfolio(portfolio);
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
                    System.out.println("Exiting program...");
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

    // Add Asset to Portfolio
    public static void addAsset(Investment portfolio) {
        System.out.println("\nAdding a new Asset:");
        Asset newAsset = createAsset("new");
        portfolio.addAsset(newAsset);
        System.out.println("Asset added successfully!");
    }


    // Edit Asset in Portfolio
    public static void editAsset(Investment portfolio) {
        System.out.println("\nEditing an Asset:");
        Asset oldAsset = createAsset("old");
        Asset newAsset = createAsset("new");
        portfolio.editAsset(oldAsset, newAsset);
    }

    // Remove Asset from Portfolio
    public static void removeAsset(Investment portfolio) {
        System.out.println("\nRemoving an Asset:");
        Asset assetToRemove = createAsset("remove");
        portfolio.removeAsset(assetToRemove);
        System.out.println("Asset removed successfully!");
    }

    // Show Portfolio Details
    public static void showPortfolio(Investment portfolio) {
        System.out.println("\nPortfolio Details:");
        portfolio.listAssets();
        System.out.printf("\nTotal Portfolio Value: $%.2f%n", portfolio.getCurrentValue());
        System.out.printf("Portfolio Risk Score: %.2f%n", portfolio.calculateRiskScore());
        System.out.printf("ROI: %.2f%%%n", portfolio.recordROI());
        portfolio.displayAssetTypes();
        System.out.println("\nAsset Distribution:");
        portfolio.recordAssetDistribution().forEach((type, percent) -> {
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
}
