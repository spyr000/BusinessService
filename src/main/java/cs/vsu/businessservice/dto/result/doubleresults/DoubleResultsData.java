package cs.vsu.businessservice.dto.result.doubleresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoubleResultsData {
    private Double retentionOfNewCustomersCost;
    private Double newEquipmentCost;
    private Double amortizationCost;
    private Double logisticsCost;
    private Double fotIncreasingCost;
    private Double minimalInvestments;
}
