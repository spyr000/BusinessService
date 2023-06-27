package cs.vsu.businessservice.service;

import cs.vsu.businessservice.dto.result.doubleresults.DoubleResultsResponse;
import cs.vsu.businessservice.dto.result.investmentsresults.InvestmentsResultsResponse;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsResponse;

public interface ResultsCalculatorService {
    NormalResultsResponse calculateNormalResults(long projectId);

    DoubleResultsResponse calculateDoubleResults(long projectId);

    InvestmentsResultsResponse calculateInvestmentsResults(long projectId);
}
