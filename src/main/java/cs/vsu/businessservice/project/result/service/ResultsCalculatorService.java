package cs.vsu.businessservice.project.result.service;

import cs.vsu.businessservice.project.result.dto.doubleresults.DoubleResultsResponse;
import cs.vsu.businessservice.project.result.dto.investmentsresults.InvestmentsResultsResponse;
import cs.vsu.businessservice.project.result.dto.normalresults.NormalResultsResponse;

public interface ResultsCalculatorService {
    NormalResultsResponse calculateNormalResults(long projectId);

    DoubleResultsResponse calculateDoubleResults(long projectId);

    InvestmentsResultsResponse calculateInvestmentsResults(long projectId);
}
