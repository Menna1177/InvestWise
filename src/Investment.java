import java.io.*;
import java.time.LocalDate;
import java.util.*;
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
        Investment.userName = userName;
        this.metrics = new Investment_Metrics(0);
        this.lastUpdated = LocalDate.now();
        this.goalTracker = new Goal_Manager();
        this.currentValue = investmentAssets.stream().mapToDouble(Asset::getValue).sum();
    }

    public Map<Risk_Cat, List<Asset>> getAssetRisk() {
        return investmentAssets.stream()
                .collect(Collectors.groupingBy(Asset::getRiskCategory));
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Investment.userName = userName;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public List<Asset> getInvestmentAssets() {
        return new ArrayList<>(investmentAssets);
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
        saveToFile(userName);
    }

    public static void AddAsset(Investment portfolio) {
        System.out.println("\nAdding a new Asset:");
        Asset newAsset = InvestmentManagement.createAsset("new");
        portfolio.addAsset(newAsset);
        System.out.println("Asset added successfully!");
    }

    public void listAssets() {
        List<Asset> loadedAssets = loadFromFile(userName).getInvestmentAssets();

        if (loadedAssets.isEmpty()) {
            System.out.println("No assets found.");
            return;
        }

        System.out.println("Current Investment Portfolio:");
        System.out.printf("%-20s %-15s %-10s %-15s %-10s%n",
                "Name", "Type", "Value", "Risk", "ROI");

        loadedAssets.forEach(asset -> System.out.printf("%-20s %-15s $%-9.2f %-15s %-10.2f%%%n",
                asset.getName(),
                asset.getType(),
                asset.getValue(),
                asset.getRiskCategory(),
                asset.getRoi()));
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
        saveToFile(userName);
        System.out.println("Asset updated successfully.");
    }

    public void removeAsset(Asset asset) {
        if (!investmentAssets.contains(asset)) {
            System.out.println("Asset not found in portfolio");
            return;
        }

        investmentAssets.remove(asset);
        currentValue -= asset.getValue();
        metrics.updateMetrics(asset.getType(), -asset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
        saveToFile(userName);
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

    @SuppressWarnings("unchecked")
    public static Investment loadFromFile(String username) {
        File file = new File(username + "_investment.ser");
        if (!file.exists()) {
            System.out.println("No investment file found. Starting with a new portfolio.");
            return new Investment(username);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Investment inv = (Investment) in.readObject();
            return inv;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading investment: " + e.getMessage());
            return new Investment(username);
        }
    }


    public void saveToFile(String username) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(username + "_investment.ser"))) {
            out.writeObject(this);
            System.out.println("Investment object saved successfully for user: " + username);
        } catch (IOException e) {
            System.out.println("Error saving investment: " + e.getMessage());
        }
    }

}
