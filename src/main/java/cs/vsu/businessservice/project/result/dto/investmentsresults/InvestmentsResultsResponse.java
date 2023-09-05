package cs.vsu.businessservice.project.result.dto.investmentsresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvestmentsResultsResponse {

    private Long projectId;

    private InvestmentsResultsData investmentsResultsData;
}
