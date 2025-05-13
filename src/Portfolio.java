import java.util.*;

public class Portfolio {
    private List<Investment> MyInvestment;
    private List<Goal> MyGoals;
    private Zakat_Calc MyZakat;

    public Portfolio(){
        this.MyInvestment = new ArrayList<>();
        this.MyGoals = new ArrayList<>();
    }

    /**
     *
     * @param investment
     */
    public void setMyInvestment(Investment investment) {
        if (investment != null) {
            MyInvestment.add(investment);
        }
    }

    /**
     *
     * @param goal
     */
    public void setMyGoals(Goal goal){
        if (goal != null){
            MyGoals.add(goal);
        }
    }

    /**
     *
     * @param zakat
     */
    public void setMyZakat(Zakat_Calc zakat){
        this.MyZakat = zakat;
    }

    /**
     *
     * @return
     */
    public double calcTotalRisk(){
        return 0;
    }

    /**
     *
     * @return
     */
    public double calcTotalValue(){
        return 0;
    }

    /**
     *
     * @return
     */
    public List<Asset> getLiquidAssets(){
        Set<Asset> uniqueAssets = new HashSet<>();
        for(Investment inv : MyInvestment){
            if (inv != null) {
                for (Asset asset : inv.getInvestmentAssets()) {
                    if (asset.isLiquid()) {
                        uniqueAssets.add(asset);
                    }
                }
            }
        }
        return new ArrayList<>(uniqueAssets);
    }

}
