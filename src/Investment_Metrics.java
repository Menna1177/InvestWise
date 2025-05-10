import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.io.Serializable;

public class Investment_Metrics implements Serializable {
    private static final long serialVersionUID = 1L;

    private double current_ROI;
    private Map<Asset_Type, Double> asset_distribution;  // Store asset distribution in percentage
    private TreeMap<LocalDate, Double> valueSnapshots;   // Using TreeMap for sorted dates
    private double initialInvestment;

    // Constructor to initialize initial investment
    public Investment_Metrics(double initialInvestment) {
        this.initialInvestment = initialInvestment;
        this.asset_distribution = new TreeMap<>();
        this.valueSnapshots = new TreeMap<>();
    }

    // Calculates Return on Investment (ROI) as a percentage
    public double calculateROI() {
        if (valueSnapshots.isEmpty() || initialInvestment == 0) {
            return 0.0; // Return 0 if there's no data or initial investment is zero
        }

        Double latestValue = valueSnapshots.lastEntry().getValue();
        this.current_ROI = ((latestValue - initialInvestment) / initialInvestment) * 100;
        return current_ROI;
    }

    // Returns asset distribution with percentages
    public Map<Asset_Type, Double> getAssetDistribution() {
        // Calculate percentages if the total sum is not 100%
        double total = asset_distribution.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total != 100.0 && total != 0) {
            asset_distribution.replaceAll((k, v) -> (v / total) * 100); // Adjust to percentage
        }
        return new TreeMap<>(asset_distribution); // Return a new TreeMap to avoid direct modification
    }

    // Returns valuation trends with dates sorted chronologically
    public Map<LocalDate, Double> getValuationTrends() {
        return new TreeMap<>(valueSnapshots); // Returns sorted map of value trends
    }

    // Updates metrics with new data (asset type, value change, and date)
    public void updateMetrics(Asset_Type type, double valueChange, LocalDate date) {
        // Update asset distribution
        asset_distribution.merge(type, valueChange, Double::sum);

        // Update value snapshots: either take last value or initial investment if it's empty
        double currentTotal = valueSnapshots.isEmpty() ? initialInvestment : valueSnapshots.lastEntry().getValue();
        valueSnapshots.put(date, currentTotal + valueChange); // Store the updated value for the date

        // Recalculate ROI after updating metrics
        calculateROI();
    }

    // Get current ROI value
    public double getCurrentROI() {
        return current_ROI;
    }

    // Get initial investment value
    public double getInitialInvestment() {
        return initialInvestment;
    }

}
