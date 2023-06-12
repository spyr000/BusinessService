package cs.vsu.businessservice.dto.result.doubleresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoubleResultsResponse {

    private Long projectId;

    private DoubleResultsData doubleResultsData;
}
