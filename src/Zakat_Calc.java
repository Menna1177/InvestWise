import java.util.ArrayList;
import java.util.List;

public class Zakat_Calc {
    private static final double NISAB = 5000;
    private double zakat_rate;

    public Zakat_Calc( double zakat){
        this.zakat_rate = zakat;
    }

    public double calculateZakat(Portfolio portfolio){
        List<Asset> liquidInvestment = new ArrayList<>();
        liquidInvestment = portfolio.getLiquidAssets();
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-10s%n", "" ,"Asset Name", "Asset Value" ,"Zakat Amount");
        for (Asset asset : liquidInvestment) {
            if (isZakatApplicable(asset)) {
                double zakat = asset.getValue() * zakat_rate;
                System.out.printf("%-5s %-17s %-16s %-10s%n", "" ,asset.getName() , asset.getValue() , zakat);
            }
            else{
                System.out.printf("%-5s %-15s %-15s %-10s%n", asset.getName() , "not applicable");
            }

        }
        System.out.println("------------------------------------------------------------------------");
        return 0;
    }

    public boolean isZakatApplicable (Asset asset){
        return asset.getValue() >= NISAB && asset.getHoldingYears() >= 1 && asset.isHalal() ;
    }


}
