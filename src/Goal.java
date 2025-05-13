import java.io.Serializable;
import java.time.LocalDate;

public class Goal implements Serializable {
    private static final long serialVersionUID = 1L;
    String type;
    double amount;
    LocalDate deadline;
    double currrentProgress;
    double autoSaveAmount;
    boolean EnableAutoSave;

    /**
     *
     * @param amount
     * @param type
     * @param deadline
     * @param currrentProgress
     * @param autoSaveAmount
     * @param enableAutoSave
     */
    Goal(double amount, String type, LocalDate deadline, double currrentProgress, double autoSaveAmount, boolean enableAutoSave) {
        this.amount = amount;
        this.type = type;
        this.deadline = deadline;
        this.currrentProgress = currrentProgress;
        this.autoSaveAmount = autoSaveAmount;
        this.EnableAutoSave = enableAutoSave;
    }

    /**
     *
     * @return
     */
    boolean IsAchieved() {
        return currrentProgress >= amount;
    }


    /**
     *
     * @return
     */
    // Returns how much is left to reach the goal
    double getRemainingAmount() {
        return Math.max(0, amount - currrentProgress);
    }

    /**
     *
     * @return
     */
    // Checks if the goal is past its deadline
    boolean isOverdue() {
        return LocalDate.now().isAfter(deadline);
    }

    /**
     *
     * @param amountToAdd
     */
    // Adds to the current progress
    void addProgress(double amountToAdd) {
        if (amountToAdd > 0) {
            this.currrentProgress += amountToAdd;
        }
    }

    /**
     *
     * @return
     */
    // Returns the percentage of completion (0.0 to 1.0)
    double getCompletionPercentage() {
        return Math.min(1.0, currrentProgress / amount);
    }
}
