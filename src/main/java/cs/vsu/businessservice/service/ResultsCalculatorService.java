package cs.vsu.businessservice.service;

import cs.vsu.businessservice.dto.result.normalresults.NormalResultsResponse;

public interface ResultsCalculatorService {
    NormalResultsResponse calculateNormalResults(long projectId);
}
