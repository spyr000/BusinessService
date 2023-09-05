package cs.vsu.businessservice.project.result.dto.normalresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NormalResultsPercents {
    private Double incomePercent;
    private Double costPricePercent;
    private Double grossProfitPercent;
    private Double marketingAndAdvertisingPercent;
    private Double rentPercent;
    private Double logisticsPercent;
    private Double fotPercent;
    private Double otherExpensesPercent;
    private Double incomeTaxPercent;
    private Double netProfitPercent;

    public double[] getData() {
        return new double[] {
                this.incomePercent,
                this.costPricePercent,
                this.grossProfitPercent,
                this.marketingAndAdvertisingPercent,
                this.rentPercent,
                this.logisticsPercent,
                this.fotPercent,
                this.otherExpensesPercent,
                this.incomeTaxPercent,
                this.netProfitPercent
        };
    }
}
