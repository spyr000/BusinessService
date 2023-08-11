package cs.vsu.businessservice.service.impl;

import cs.vsu.businessservice.dto.result.doubleresults.DoubleResultsData;
import cs.vsu.businessservice.dto.result.doubleresults.DoubleResultsResponse;
import cs.vsu.businessservice.dto.result.investmentsresults.InvestmentsResultsData;
import cs.vsu.businessservice.dto.result.investmentsresults.InvestmentsResultsResponse;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsData;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsPercents;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsResponse;
import cs.vsu.businessservice.entity.*;
import cs.vsu.businessservice.exception.EntityNotFoundException;
import cs.vsu.businessservice.repo.*;
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
    private final InvestmentsRepo investmentsRepo;

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
        var proceedPercent = Optional.ofNullable(economic.getProceedPercent()).orElse(0.);
        var income = Optional.ofNullable(economic.getAveragePrice()).orElse(0.)
                * Optional.ofNullable(economic.getClientsAmt()).orElse(0L).doubleValue()
                * Optional.ofNullable(economic.getOrdersCnt()).orElse(0L).doubleValue();
        var costPrice = income * proceedPercent / 100;
        var coefficient = projectRepo
                .findById(projectId)
                .orElseThrow(
                        () -> new EntityNotFoundException(Project.class)
                ).getYearsCount()
                * 12.;
        var grossProfit = income - costPrice;
        var amortization = 0.;
        var fot = (double) Optional.ofNullable(
                fixedExpenses.getWageFundCost()
        ).orElse(0.);
        var logistics = (double) Optional.ofNullable(
                variableExpenses.getLogisticsCost() * coefficient
        ).orElse(0.);
        var marketingAndAdvertising = (double) Optional.ofNullable(
                fixedExpenses.getMarketingCost() * coefficient
        ).orElse(0.);
        var rent = (double) Optional.ofNullable(
                fixedExpenses.getOfficeRentalCost() * coefficient
        ).orElse(0.);
        var otherExpenses = (double) Optional.ofNullable(
                variableExpenses.getOtherExpensesCost() * coefficient
        ).orElse(0.);
        var profitBeforeTax = (double) Optional.ofNullable(
                grossProfit
                - logistics
                - marketingAndAdvertising
                - rent
                - otherExpenses
                - amortization
                - fot
        ).orElse(0.);
        var tax = (double) Optional.ofNullable(
                profitBeforeTax * fixedExpenses.getIncomeTaxPercent() / 100
        ).orElse(0.);
        var netProfit = (double) Optional.ofNullable(profitBeforeTax - tax).orElse(0.);

        normalResultsData.setIncome(income);
        normalResultsData.setCostPrice(costPrice);
        normalResultsData.setGrossProfit(grossProfit);
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
        result.setProjectId(projectId);
        return result;
    }

    @Override
    public DoubleResultsResponse calculateDoubleResults(long projectId) {

        var economic = economicRepo.findEconomicByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(Economic.class)
        );
        var variableExpenses = variableExpensesRepo.findVariableExpensesByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(VariableExpenses.class)
        );
        var fixedExpenses = fixedExpensesRepo.findFixedExpensesByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(FixedExpenses.class)
        );
//        var investments = investmentsRepo.findInvestmentsByProjectId(projectId).orElseThrow(
//                () -> new EntityNotFoundException(Investments.class)
//        );

        var doubleResultsData = new DoubleResultsData();
        var retainingNewCustomers = Optional.ofNullable(economic.getClientAttractionCost() * economic.getClientsAmt() * economic.getClientsOutflowPercent() * 12. / 100.).orElse(0.);
        var newEquipmentCost = Optional.ofNullable(fixedExpenses.getEquipmentServiceCost()).orElse(0.);
        var newLogisticsCost = Optional.ofNullable(variableExpenses.getLogisticsCost()).orElse(0.);
        var newWageFundCost = Optional.ofNullable(fixedExpenses.getWageFundCost() * 0.5).orElse(0.);
        var minInvestment = Optional.ofNullable(retainingNewCustomers + newEquipmentCost + newLogisticsCost + newWageFundCost).orElse(0.);
        var amortization = 0.;
        doubleResultsData.setLogisticsCost(newLogisticsCost);
        doubleResultsData.setFotIncreasingCost(newWageFundCost);
        doubleResultsData.setNewEquipmentCost(newEquipmentCost);
        doubleResultsData.setRetentionOfNewCustomersCost(retainingNewCustomers);
        doubleResultsData.setMinimalInvestments(minInvestment);

        var result = new DoubleResultsResponse();
        result.setDoubleResultsData(doubleResultsData);
        result.setProjectId(projectId);
        return result;
    }

    @Override
    public InvestmentsResultsResponse calculateInvestmentsResults(long projectId) {
        var investments = investmentsRepo.findInvestmentsByProjectId(projectId).orElseThrow(
                () -> new EntityNotFoundException(Investments.class)
        );

        var investmentsResultsData = new InvestmentsResultsData();
        var costBroughtCustomer = Optional.ofNullable(investments.getClickConversionPercent()
                * investments.getConversionToApplicationsPercent()
                * investments.getRequestsToPurchasesConversionPercent() * investments.getShowingCost() / 100000.)
                .orElse(0.);
        var recruitmentCostsClient = Optional.ofNullable(costBroughtCustomer
                * investments.getCustomerGrowth())
                .orElse(0.);
        var subscriberAcquisitionCosts = Optional.ofNullable(investments.getCustomerCost()
                        * investments.getMonthGrowth())
                .orElse(0.);
        var marketingExpenses = Optional.ofNullable(subscriberAcquisitionCosts
                        + recruitmentCostsClient)
                .orElse(0.);
        investmentsResultsData.setMarketingExpenses(marketingExpenses);
        investmentsResultsData.setRecruitmentCostsClient(recruitmentCostsClient);
        investmentsResultsData.setCostBroughtCustomer(costBroughtCustomer);
        investmentsResultsData.setSubscriberAcquisitionCosts(subscriberAcquisitionCosts);

        var result = new InvestmentsResultsResponse();
        result.setInvestmentsResultsData(investmentsResultsData);
        result.setProjectId(projectId);
        return result;
    }
}
