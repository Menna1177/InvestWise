import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Investment implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Asset> investmentAssets = new ArrayList<>();
    private double currentValue;
    private Goal_Manager goalTracker;
    private Investment_Metrics metrics;
    private LocalDate lastUpdated;
    private static String username;
    public Investment(String username) {
        this.username = username;
        this.metrics = new Investment_Metrics(0);
        this.lastUpdated = LocalDate.now();
        this.goalTracker = new Goal_Manager();
    }

    public Map<Risk_Cat, List<Asset>> getAssetRisk() {
        return investmentAssets.stream()
                .collect(Collectors.groupingBy(Asset::getRiskCategory));
    }

    void setUserName(String username) {
        this.username = username;
    }
  public static String getUsername() {
        return username;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void displayAssetTypes() {
        Map<Asset_Type, Long> typeCounts = investmentAssets.stream()
                .collect(Collectors.groupingBy(Asset::getType, Collectors.counting()));

        System.out.println("Asset Type Distribution:");
        typeCounts.forEach((type, count) ->
                System.out.printf("- %s: %d assets%n", type, count));
    }

    public double calculateRiskScore() {
        if (investmentAssets.isEmpty()) return 0.0;
        double totalRisk = investmentAssets.stream()
                .mapToDouble(a -> a.getValue() * a.getRiskCategory().getScore())
                .sum();
        return (totalRisk / currentValue) / 4.0;
    }

    public void addAsset(Asset asset) {
        if (asset == null) throw new IllegalArgumentException("Asset cannot be null");
        investmentAssets.add(asset);
        currentValue += asset.getValue();
        metrics.updateMetrics(asset.getType(), asset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
        saveToFile();
    }

    public static void Add(Investment portfolio) {
        System.out.println("\nAdding a new Asset:");
        Asset newAsset = InvestmentManagement.createAsset("new");
        portfolio.addAsset(newAsset);
        portfolio.saveToFile();
        System.out.println("Asset added successfully!");
    }

    public void listAssets() {
        System.out.println("Current Investment Portfolio:");
        System.out.printf("%-20s %-15s %-10s %-15s %-10s%n",
                "Name", "Type", "Value", "Risk", "ROI");

        investmentAssets.forEach(asset ->
                System.out.printf("%-20s %-15s $%-9.2f %-15s %-10.2f%%%n",
                        asset.getName(),
                        asset.getType(),
                        asset.getValue(),
                        asset.getRiskCategory(),
                        asset.getRoi()));
        saveToFile();
    }

    public void editAsset(Asset oldAsset, Asset newAsset) {
        if (!investmentAssets.contains(oldAsset)) {
            System.out.println("Asset not found in portfolio.");
            return;
        }
        int index = investmentAssets.indexOf(oldAsset);
        if (oldAsset.equals(newAsset)) {
            System.out.println("The new asset is identical to the old one.");
            return;
        }

        currentValue -= oldAsset.getValue();
        currentValue += newAsset.getValue();
        investmentAssets.set(index, newAsset);
        metrics.updateMetrics(newAsset.getType(), newAsset.getValue() - oldAsset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
        System.out.println("Asset updated successfully.");
        saveToFile();
    }


    public void removeAsset(Asset asset) {
        if (!investmentAssets.contains(asset.getName())) {
            System.out.println("Asset not found in portfolio");
            return;
        }

        investmentAssets.remove(asset);
        currentValue -= asset.getValue();
        metrics.updateMetrics(asset.getType(), -asset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
        saveToFile();

        System.out.println("Asset removed successfully!");
    }


    public void setGoalTracker(Goal_Manager goalTracker) {
        this.goalTracker = goalTracker;
    }

    public Goal_Manager getGoalTracker() {
        return this.goalTracker;
    }


    public void notifyTracker() {
        if (goalTracker != null) {
            goalTracker.trackProgress();
        }
    }

    public double recordROI() {
        return metrics.calculateROI();
    }

    public Map<Asset_Type, Double> recordAssetDistribution() {
        return metrics.getAssetDistribution();
    }

    public Map<LocalDate, Double> recordValuationTrends() {
        return metrics.getValuationTrends();
    }

    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(username + "_investment.ser"))) {
            out.writeObject(this);
            System.out.println("Investment data saved for user: " + username);
        } catch (IOException e) {
            System.out.println("Error saving investment data: " + e.getMessage());
        }
    }

    public static Investment loadFromFile(String username) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(username + "_investment.ser"))) {
            Investment investment = (Investment) in.readObject();
            if (investment.username == null) {
                investment.setUserName(username);
            }
            return investment;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading investment data: " + e.getMessage());
            Investment newInvestment = new Investment(username);
            return newInvestment;
        }
    }
}
