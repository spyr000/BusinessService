package cs.vsu.businessservice.dto.result.investmentsresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvestmentsResultsData {
    private Double costBroughtCustomer;
    private Double recruitmentCostsClient;
    private Double subscriberAcquisitionCosts;
    private Double marketingExpenses;

    public double[] getData() {
        return new double[] {
                this.costBroughtCustomer,
                this.recruitmentCostsClient,
                this.subscriberAcquisitionCosts,
                this.marketingExpenses
        };
    }
}
