package cs.vsu.businessservice.project.result.dto.doubleresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoubleResultsResponse {

    private Long projectId;

    private DoubleResultsData doubleResultsData;
}
