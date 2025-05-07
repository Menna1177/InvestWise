import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class Investment_Metrics {
    private double current_ROI;
    private Map<Asset_Type, Double> asset_distribution;  // Changed to store percentage values
    private TreeMap<LocalDate, Double> valueSnapshots;   // Using TreeMap for sorted dates
    private double initialInvestment;

    public Investment_Metrics(double IinitialInvestment) {
        this.initialInvestment = InitialInvestment;
        this.asset_distribution = new TreeMap<>();
        this.valueSnapshots = new TreeMap<>();
    }

    // Calculates Return on Investment (ROI) as a percentage
    public double calculateROI() {
        if (valueSnapshots.isEmpty() || initialInvestment == 0) {
            return 0.0;
        }

        Double latestValue = valueSnapshots.lastEntry().getValue();
        this.current_ROI = ((latestValue - initialInvestment) / initialInvestment) * 100;
        return current_ROI;
    }

    // Returns asset distribution with percentages
    public Map<Asset_Type, Double> getAssetDistribution() {
        // Calculate percentages if needed (assuming total is 100%)
        double total = asset_distribution.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total != 100.0 && total != 0) {
            asset_distribution.replaceAll((k, v) -> (v / total) * 100);
        }
        return new TreeMap<>(asset_distribution);
    }

    // Returns valuation trends with dates sorted chronologically
    public Map<LocalDate, Double> getValuationTrends() {
        return new TreeMap<>(valueSnapshots);
    }

    // Updates metrics with new data
    public void updateMetrics(Asset_Type type, double valueChange, LocalDate date) {
        // Update asset distribution
        asset_distribution.merge(type, valueChange, Double::sum);

        // Update value snapshots
        double currentTotal = valueSnapshots.isEmpty() ? initialInvestment : valueSnapshots.lastEntry().getValue();
        valueSnapshots.put(date, currentTotal + valueChange);

        // Recalculate ROI
        calculateROI();
    }

    // Additional helper methods
    public double getCurrentValue() {
        return valueSnapshots.isEmpty() ? initialInvestment : valueSnapshots.lastEntry().getValue();
    }

    public double getCurrentROI() {
        return current_ROI;
    }

    public void addSnapshot(LocalDate date, double value) {
        valueSnapshots.put(date, value);
    }

    public void setAssetDistribution(Map<Asset_Type, Double> distribution) {
        this.asset_distribution = new TreeMap<>(distribution);
    }
}