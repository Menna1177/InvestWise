import java.time.LocalDate;

public class Goal {
    String type;
    double amount;
    LocalDate deadline;
    double currrentProgress;
    double autoSaveAmount;
    boolean EnableAutoSave;

    Goal(double amount, String type, LocalDate deadline, double currrentProgress, double autoSaveAmount, boolean enableAutoSave) {
        this.amount = amount;
        this.type = type;
        this.deadline = deadline;
        this.currrentProgress = currrentProgress;
        this.autoSaveAmount = autoSaveAmount;
        this.EnableAutoSave = enableAutoSave;
    }

    boolean IsAchieved() {
        return currrentProgress >= amount;
    }


    // Returns how much is left to reach the goal
    double getRemainingAmount() {
        return Math.max(0, amount - currrentProgress);
    }

    // Checks if the goal is past its deadline
    boolean isOverdue() {
        return LocalDate.now().isAfter(deadline);
    }

    // Adds to the current progress
    void addProgress(double amountToAdd) {
        if (amountToAdd > 0) {
            this.currrentProgress += amountToAdd;
        }
    }

    // Returns the percentage of completion (0.0 to 1.0)
    double getCompletionPercentage() {
        return Math.min(1.0, currrentProgress / amount);
    }
}