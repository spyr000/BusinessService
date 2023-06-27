package cs.vsu.businessservice.dto.result.investmentsresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvestmentsResultsResponse {

    private Long projectId;

    private InvestmentsResultsData investmentsResultsData;
}
