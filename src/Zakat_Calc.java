import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Zakat_Calc {
    private static final double NISAB = 5000;
    private double zakat_rate;

    public Zakat_Calc( double zakat){
        this.zakat_rate = zakat;
    }

    /**
     * calculateZakat method.
     * @param portfolio the portfolio
     * @return result of the operation
     */
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
                System.out.printf("%-5s %-15s %-15s %-10s%n", "",asset.getName() ,asset.getValue(), "not applicable");
            }

        }
        System.out.println("------------------------------------------------------------------------");
        return 0;
    }

    /**
     * isZakatApplicable method.
     * @param asset the asset
     * @return result of the operation
     */
    public boolean isZakatApplicable (Asset asset){
        return asset.getValue() >= NISAB && asset.getHoldingYears() >= 1 && asset.isHalal() ;
    }

    /**
     * generateZakatReport method.
     * @param portfolio the portfolio
     * @param filename the filename
     */
    public void generateZakatReport(Portfolio portfolio , String filename){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document , new FileOutputStream(filename));
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 22 , BaseColor.BLACK );
            Paragraph title = new Paragraph("Zakat Report" , titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);


            Font normalFont = FontFactory.getFont(
                    FontFactory.HELVETICA,  // نوع الخط
                    12,                     // حجم الخط
                    BaseColor.BLACK         // لون الخط
            );

            Font boldFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    14,
                    BaseColor.DARK_GRAY
            );

            PdfPTable table = new PdfPTable(5);
            table.addCell(createStyledCell("Asset Name" ,boldFont ));
            table.addCell(createStyledCell("Asset Type",boldFont));
            table.addCell(createStyledCell("Value",boldFont));
            table.addCell(createStyledCell("Zakat Amount" , boldFont));
            table.addCell(createStyledCell("Risk" , boldFont));

            for (Asset asset : portfolio.getLiquidAssets()) {
                if (isZakatApplicable(asset)) {
                    table.addCell(createStyledCell(asset.getName(),normalFont));
                    table.addCell(createStyledCell(String.valueOf(asset.getType()),normalFont));
                    table.addCell(createStyledCell(String.valueOf(asset.getValue()),normalFont));
                    table.addCell(createStyledCell(String.valueOf(asset.getValue() * zakat_rate),normalFont));
                    table.addCell(createStyledCell(String.valueOf(asset.getRiskCategory()),normalFont));
                }
            }
            document.add(table);
            document.close();
            System.out.println("Report saved as: " + filename);
        }
        catch (Exception e){
            System.out.println("Failed to generate PDF: " + e.getMessage());
        }
    }
    /**
     * createStyledCell method.
     * @param text the text
     * @param font the font
     * @return result of the operation
     */
    public PdfPCell createStyledCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(15);             // حشوة داخلية
        cell.setBackgroundColor(BaseColor.WHITE);  // لون الخلفية
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  // محاذاة
        return cell;
    }
}
