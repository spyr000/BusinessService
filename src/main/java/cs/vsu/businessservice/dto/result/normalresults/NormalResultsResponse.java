package cs.vsu.businessservice.dto.result.normalresults;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NormalResultsResponse {
    private Long projectId;
    private NormalResultsData normalResultsData;
    private NormalResultsPercents normalResultsPercents;
}
