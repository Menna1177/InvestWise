import java.time.LocalDate;
import java.util.Arrays;

public class Goal_Manager {
    private Goal[] goals;
    private Investment tracked;
    private int goalCount; // To track actual number of goals

    public Goal_Manager() {
        this.goals = new Goal[10]; // Initial capacity
        this.goalCount = 0;
    }

    public Goal_Manager(int initialCapacity) {
        this.goals = new Goal[initialCapacity];
        this.goalCount = 0;
    }

    // Adds a goal, automatically expanding array if needed
    public void addGoal(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal cannot be null");
        }

        // Resize array if full
        if (goalCount == goals.length) {
            goals = Arrays.copyOf(goals, goals.length * 2);
        }

        goals[goalCount++] = goal;
    }

    // Displays all goals with proper formatting
    public void displayGoals() {
        if (goalCount == 0) {
            System.out.println("No goals to display.");
            return;
        }

        System.out.println("Current Goals:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %-12s %-12s %-10s%n",
                "#", "Type", "Target", "Progress", "Deadline", "Status");

        for (int i = 0; i < goalCount; i++) {
            Goal goal = goals[i];
            String status = goal.IsAchieved() ? "Achieved" :
                    (goal.deadline.isBefore(LocalDate.now()) ? "Overdue" : "In Progress");

            System.out.printf("%-5d %-20s $%-9.2f $%-11.2f %-12s %-10s%n",
                    i + 1,
                    goal.type,
                    goal.amount,
                    goal.currrentProgress,
                    goal.deadline.toString(),
                    status);
        }
        System.out.println("--------------------------------------------------");
    }

    // Tracks progress of all goals
    public void trackProgress() {
        for (int i = 0; i < goalCount; i++) {
            Goal goal = goals[i];
            if (goal.EnableAutoSave) {
                goal.currrentProgress += goal.autoSaveAmount;
            }
        }
    }

    // Gets a goal by index
    public Goal getGoal(int index) {
        if (index < 0 || index >= goalCount) {
            throw new IndexOutOfBoundsException("Invalid goal index");
        }
        return goals[index];
    }

    // Removes a goal by index
    public void removeGoal(int index) {
        if (index < 0 || index >= goalCount) {
            throw new IndexOutOfBoundsException("Invalid goal index");
        }

        // Shift remaining elements
        for (int i = index; i < goalCount - 1; i++) {
            goals[i] = goals[i + 1];
        }

        goals[--goalCount] = null; // Clear last reference
    }

    // Returns number of goals
    public int getGoalCount() {
        return goalCount;
    }

    // Links an investment to track for goals
    public void setTrackedInvestment(Investment investment) {
        this.tracked = investment;
    }

    // Gets the tracked investment
    public Investment getTrackedInvestment() {
        return tracked;
    }
}