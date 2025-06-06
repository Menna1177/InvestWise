import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.io.Serializable;

public class Asset implements Serializable {
    private String name;
    private double currentValue;
    private Asset_Type type;
    private boolean halal;
    private LocalDate purchaseDate;
    private Double purchasePrice;
    private Risk_Cat riskCategory;
    private double annualGrowthRate;

    /**
     *
     * @param name
     * @param currentValue
     * @param type
     * @param halal
     * @param purchaseDate
     * @param purchasePrice
     * @param riskCategory
     */
    public Asset(String name, double currentValue, Asset_Type type, boolean halal,
                 LocalDate purchaseDate, Double purchasePrice, Risk_Cat riskCategory) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset name cannot be null or empty");
        }
        if (currentValue <= 0) {
            throw new IllegalArgumentException("Asset value must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Asset type cannot be null");
        }
        if (riskCategory == null) {
            throw new IllegalArgumentException("Risk category cannot be null");
        }

        this.name = name;
        this.currentValue = currentValue;
        this.type = type;
        this.halal = halal;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.riskCategory = riskCategory;

        // Calculate initial growth rate if purchase data exists
        if (purchasePrice != null && purchaseDate != null) {
            this.annualGrowthRate = calculateAnnualGrowthRate();
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Asset asset = (Asset) obj;
        return name.equals(asset.name) && type == asset.type;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    /**
     *
     * @return
     */
    // Calculates annualized return percentage
    public double calculateAnnualGrowthRate() {
        if (purchasePrice == null || purchasePrice == 0.0 || purchaseDate == null) {
            return 0.0;
        }

        long yearsHeld = getHoldingYears();
        if (yearsHeld == 0) yearsHeld = 1; // prevent division by zero

        double totalReturn = (currentValue - purchasePrice) / purchasePrice;
        return Math.pow(1 + totalReturn, 1.0/yearsHeld) - 1;
    }

    /**
     *
     * @param riskAdjustment
     * @return
     */
    // Calculates risk-adjusted return with proper bounds checking
    public double calculateRiskAdjustedReturn(double riskAdjustment) {
        if (riskAdjustment < 0 || riskAdjustment > 1) {
            throw new IllegalArgumentException("Risk adjustment must be between 0 and 1");
        }

        double estimatedReturn = calculateAnnualGrowthRate();
        return estimatedReturn * (1 - riskAdjustment);
    }

    /**
     *
     * @return
     */
    public long getHoldingYears() {
        if (purchaseDate == null) return 0;
        return ChronoUnit.YEARS.between(purchaseDate, LocalDate.now());
    }

    /**
     *
     * @return
     */
    // Getters
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public double getValue() {
        return currentValue;
    }

    /**
     *
     * @return
     */
    public Asset_Type getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public Risk_Cat getRiskCategory() {
        return riskCategory;
    }

    /**
     *
     * @return
     */
    public boolean isHalal() {
        return type != Asset_Type.Bond;
    }

    /**
     *
     * @return
     */
    public double getAnnualGrowthRate() {
        return annualGrowthRate;
    }

    /**
     *
     * @return
     */
    public boolean isLiquid() {
        if (type == null) return false;
        return type == Asset_Type.Stocks ||
                type == Asset_Type.Gold ||
                type == Asset_Type.Cash ||
                type == Asset_Type.RealState;
    }

    /**
     *
     * @return
     */
    // Add this method to the Asset class
    public double getRoi() {
        return this.annualGrowthRate * 100; // Return as percentage
    }

    /**
     *
     * @param currentValue
     */
    // Setters with validation
    public void setCurrentValue(double currentValue) {
        if (currentValue <= 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
        this.currentValue = currentValue;
        this.annualGrowthRate = calculateAnnualGrowthRate();
    }

    /**
     *
     * @param newValue
     */
    public void updateValue(double newValue) {
        setCurrentValue(newValue);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("Asset[name=%s, type=%s, value=%.2f, risk=%s, halal=%b, growth=%.2f%%]",
                name, type, currentValue, riskCategory, halal, annualGrowthRate * 100);
    }
}
