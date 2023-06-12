package cs.vsu.businessservice.dto.result.normalresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NormalResultsData {
    private Double income;
    private Double costPrice;
    private Double grossProfit;
    private Double marketingAndAdvertising;
    private Double rent;
    private Double logistics;
    private Double fot;
    private Double amortization;
    private Double otherExpenses;
    private Double incomeTax;
    private Double netProfit;

    public double[] getData() {
        return new double[] {
                this.income,
                this.costPrice,
                this.grossProfit,
                this.marketingAndAdvertising,
                this.rent,
                this.logistics,
                this.fot,
                this.amortization,
                this.otherExpenses,
                this.incomeTax,
                this.netProfit
        };
    }
}