import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
    static private String userName;


    public Investment(String userName) {
        this.userName = userName;
        this.metrics = new Investment_Metrics(0);
        this.lastUpdated = LocalDate.now();
        goalTracker = new Goal_Manager();
    }

    // Returns assets grouped by risk category
    public Map<Risk_Cat, List<Asset>> getAssetRisk() {
        return investmentAssets.stream()
                .collect(Collectors.groupingBy(Asset::getRiskCategory));
    }

   static public String getUserName(){
        return userName;
    }
    static public void setUserName(String userName){
        Investment.userName = userName;
    }
    public double getCurrentValue() {
        return currentValue;
    }
    public List<Asset> getInvestmentAssets() {
        return new ArrayList<>(investmentAssets) ;
    }


    // Displays all asset types with their counts
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

        // Normalize by dividing by maximum possible score (4)
        return (totalRisk / currentValue) / 4.0;
    }

    public void addAsset(Asset asset) {
        if (asset == null) throw new IllegalArgumentException("Asset cannot be null");
        investmentAssets.add(asset);
        currentValue += asset.getValue();
        metrics.updateMetrics(asset.getType(), asset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
    }
    public static void AddAsset(Investment portfolio) {
        System.out.println("\nAdding a new Asset:");
        Asset newAsset = InvestmentManagement.createAsset("new");
        portfolio.addAsset(newAsset);
        portfolio.saveToFile(userName);
        System.out.println("Asset added successfully!");
    }


    // Lists all assets with details
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
    }

    public void editAsset(Asset oldAsset, Asset newAsset) {
        if (!investmentAssets.contains(oldAsset)) {
            System.out.println("Asset not found in portfolio.");
            System.out.println("Portfolio contains: " + investmentAssets);

            return;
        }

        int index = investmentAssets.indexOf(oldAsset);
        System.out.println("Asset found at index: " + index+1);

        if (oldAsset.equals(newAsset)) {
            System.out.println("The new asset is identical to the old one.");
            return;
        }


        System.out.println("Current portfolio value before update: " + currentValue);
        currentValue -= oldAsset.getValue();
        System.out.println("Portfolio value after subtracting old asset: " + currentValue);
        currentValue += newAsset.getValue();
        System.out.println("Portfolio value after adding new asset: " + currentValue);


        investmentAssets.set(index, newAsset);
        System.out.println("Asset updated at index " + index);


        metrics.updateMetrics(newAsset.getType(), newAsset.getValue() - oldAsset.getValue(), LocalDate.now());
        System.out.println("Metrics updated");


        lastUpdated = LocalDate.now();
        System.out.println("Last updated on: " + lastUpdated);


        notifyTracker();
        System.out.println("Tracker notified.");
        saveToFile(userName);

        System.out.println("Asset updated successfully.");
    }


    public void removeAsset(Asset asset) {
        if (!investmentAssets.remove(asset)) {
            System.out.println("Asset not found in portfolio");
            return;
        }
        currentValue -= asset.getValue();
        metrics.updateMetrics(asset.getType(), -asset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
        saveToFile(userName);
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
    public void saveToFile(String username) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(username + "_investment.dat"))) {
            out.writeObject(this);
            System.out.println("Investment data saved for user: " + username);
        } catch (IOException e) {
            System.out.println("Error saving investment data: " + e.getMessage());
        }
    }

    public static Investment loadFromFile(String username) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(username + "_investment.dat"))) {
            return (Investment) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading investment data: " + e.getMessage());
            return new Investment(username); // Return empty investment if not found
        }
    }


}