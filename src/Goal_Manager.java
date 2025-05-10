import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Goal_Manager implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Goal> goals;
    private Investment tracked;

    public Goal_Manager() {
        this.goals = new ArrayList<>();
    }

    public void addGoal(Goal goal) {
        if (goal == null) throw new IllegalArgumentException("Goal cannot be null");
        goals.add(goal);
    }

    public void displayGoals() {
        if (goals.isEmpty()) {
            System.out.println("No goals to display.");
            return;
        }

        System.out.println("Current Goals:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %-12s %-12s %-10s%n",
                "#", "Type", "Target", "Progress", "Deadline", "Status");

        for (int i = 0; i < goals.size(); i++) {
            Goal goal = goals.get(i);
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

    public void trackProgress() {
        for (Goal goal : goals) {
            if (goal.EnableAutoSave) {
                goal.currrentProgress += goal.autoSaveAmount;
            }
        }
    }

    public Goal getGoal(int index) {
        return goals.get(index);
    }

    public void removeGoal(int index) {
        goals.remove(index);
    }

    public int getGoalCount() {
        return goals.size();
    }

    public void setTrackedInvestment(Investment investment) {
        this.tracked = investment;
    }

    public Investment getTrackedInvestment() {
        return tracked;
    }
}
