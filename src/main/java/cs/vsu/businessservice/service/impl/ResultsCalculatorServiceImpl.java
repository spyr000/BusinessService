package cs.vsu.businessservice.service.impl;

import cs.vsu.businessservice.dto.result.normalresults.NormalResultsData;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsPercents;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsResponse;
import cs.vsu.businessservice.entity.Economic;
import cs.vsu.businessservice.entity.FixedExpenses;
import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.entity.VariableExpenses;
import cs.vsu.businessservice.exception.EntityNotFoundException;
import cs.vsu.businessservice.repo.EconomicRepo;
import cs.vsu.businessservice.repo.FixedExpensesRepo;
import cs.vsu.businessservice.repo.ProjectRepo;
import cs.vsu.businessservice.repo.VariableExpensesRepo;
import cs.vsu.businessservice.service.ResultsCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ResultsCalculatorServiceImpl implements ResultsCalculatorService {
    private final ProjectRepo projectRepo;
    private final EconomicRepo economicRepo;
    private final FixedExpensesRepo fixedExpensesRepo;
    private final VariableExpensesRepo variableExpensesRepo;

    @Override
    public NormalResultsResponse calculateNormalResults(long projectId) {
        var economic = economicRepo.findEconomicByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(Economic.class)
        );
        var variableExpenses = variableExpensesRepo.findVariableExpensesByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(VariableExpenses.class)
        );
        var fixedExpenses = fixedExpensesRepo.findFixedExpensesByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(FixedExpenses.class)
        );

        var normalResultsData = new NormalResultsData();
        var proceedPercent = Optional.of(economic.getProceedPercent()).orElse(0.);
        var income = Optional.of(economic.getAveragePrice()).orElse(0.)
                * Optional.of(economic.getClientsAmt()).orElse(0L).doubleValue()
                * Optional.of(economic.getOrdersCnt()).orElse(0L).doubleValue();
        var costPrice = income * proceedPercent / 100;
        var coefficient = projectRepo
                .findById(projectId)
                .orElseThrow(
                        () -> new EntityNotFoundException(Project.class)
                ).getYearsCount()
                * 12.;
        var grossProfit = income - costPrice;
        //TODO: Где?
        var amortization = 0.;
        var fot = (double) Optional.of(
                fixedExpenses.getWageFundCost()
        ).orElse(0.);
        var logistics = (double) Optional.of(
                variableExpenses.getLogisticsCost() * coefficient
        ).orElse(0.);
        var marketingAndAdvertising = (double) Optional.of(
                fixedExpenses.getMarketingCost() * coefficient
        ).orElse(0.);
        var rent = (double) Optional.of(
                fixedExpenses.getOfficeRentalCost() * coefficient
        ).orElse(0.);
        var otherExpenses = (double) Optional.of(
                variableExpenses.getOtherExpensesCost() * coefficient
        ).orElse(0.);
        var profitBeforeTax = (double) Optional.of(
                grossProfit
                - logistics
                - marketingAndAdvertising
                - rent
                - otherExpenses
                - amortization
                - fot
        ).orElse(0.);
        var tax = (double) Optional.of(
                profitBeforeTax * fixedExpenses.getIncomeTaxPercent() / 100
        ).orElse(0.);
        var netProfit = (double) Optional.of(profitBeforeTax - tax).orElse(0.);

        normalResultsData.setIncome(income);
        normalResultsData.setCostPrice(costPrice);
        normalResultsData.setGrossProfit(grossProfit);
        normalResultsData.setAmortization(amortization);
        normalResultsData.setFot(fot * coefficient);
        normalResultsData.setLogistics(logistics);
        normalResultsData.setMarketingAndAdvertising(marketingAndAdvertising);
        normalResultsData.setRent(rent);
        normalResultsData.setOtherExpenses(otherExpenses);
        normalResultsData.setIncomeTax(tax);
        normalResultsData.setNetProfit(netProfit);

        var normalResultsPercents = new NormalResultsPercents();

        if (income == 0) income = 1.;
        normalResultsPercents.setIncomePercent(100.);
        normalResultsPercents.setCostPricePercent(proceedPercent);
        normalResultsPercents.setGrossProfitPercent(100. - proceedPercent);
        normalResultsPercents.setAmortizationPercent(amortization / income * 100);
        normalResultsPercents.setFotPercent(fot / income * 100);
        normalResultsPercents.setLogisticsPercent(logistics / income * 100);
        normalResultsPercents.setOtherExpensesPercent(otherExpenses / income * 100);
        normalResultsPercents.setIncomeTaxPercent(tax / income * 100);
        normalResultsPercents.setMarketingAndAdvertisingPercent(marketingAndAdvertising / income * 100);
        normalResultsPercents.setRentPercent(rent / income * 100);
        normalResultsPercents.setNetProfitPercent(netProfit / income * 100);

        var result = new NormalResultsResponse();
        result.setNormalResultsData(normalResultsData);
        result.setNormalResultsPercents(normalResultsPercents);
        return result;
    }
}
