import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Investment {
    private List<Asset> investmentAssets = new ArrayList<>();
    private double currentValue;
    private Goal_Manager goalTracker;
    private Investment_Metrics metrics;
    private LocalDate lastUpdated;

    public Investment() {
        this.metrics = new Investment_Metrics(0);
        this.lastUpdated = LocalDate.now();
    }

    // Returns assets grouped by risk category
    public Map<Risk_Cat, List<Asset>> getAssetRisk() {
        return investmentAssets.stream()
                .collect(Collectors.groupingBy(Asset::getRiskCategory));
    }

    public double getCurrentValue() {
        return currentValue;
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
            throw new IllegalArgumentException("Asset not found in portfolio");
        }

        int index = investmentAssets.indexOf(oldAsset);
        currentValue -= oldAsset.getValue();
        investmentAssets.set(index, newAsset);
        currentValue += newAsset.getValue();
        metrics.updateMetrics(newAsset.getType(), newAsset.getValue() - oldAsset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
    }

    public void removeAsset(Asset asset) {
        if (!investmentAssets.remove(asset)) {
            throw new IllegalArgumentException("Asset not found in portfolio");
        }
        currentValue -= asset.getValue();
        metrics.updateMetrics(asset.getType(), -asset.getValue(), LocalDate.now());
        lastUpdated = LocalDate.now();
        notifyTracker();
    }

    public void setGoalTracker(Goal_Manager goalTracker) {
        this.goalTracker = goalTracker;
    }

    // Notifies goal tracker about value changes
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

    // Additional helper methods
    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public int getAssetCount() {
        return investmentAssets.size();
    }

    public List<Asset> getAssetsByType(Asset_Type type) {
        return investmentAssets.stream()
                .filter(a -> a.getType() == type)
                .collect(Collectors.toList());
    }
}