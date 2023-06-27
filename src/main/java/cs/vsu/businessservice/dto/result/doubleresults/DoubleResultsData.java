package cs.vsu.businessservice.dto.result.doubleresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoubleResultsData {
    private Double retentionOfNewCustomersCost;
    private Double newEquipmentCost;
    private Double logisticsCost;
    private Double fotIncreasingCost;
    private Double minimalInvestments;

    public double[] getData() {
        return new double[] {
                this.retentionOfNewCustomersCost,
                this.newEquipmentCost,
                this.logisticsCost,
                this.fotIncreasingCost,
                this.minimalInvestments
        };
    }
}
