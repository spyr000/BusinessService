package cs.vsu.businessservice.dto.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectRequest {
    private String projectName;
    private String projectDesc;
    private Integer projectYearsCount;

    //region VariableExpenses
    private Double variableExpensesLogisticsCost;

    private Double variableExpensesOtherExpensesCost;

    private Double variableExpensesEquipmentCost;

    private Double variableExpensesOfficeToolsCost;
    //endregion
    //region FixedExpenses
    private Double fixedExpensesMarketingCost;
    private Double fixedExpensesOfficeRentalCost;
    private Double fixedExpensesWageFundCost;
    private Double fixedExpensesIncomeTaxPercent;
    private Double fixedExpensesInsuranceCost;
    private Double fixedExpensesEquipmentServiceCost;
    private Double fixedExpensesPublicUtilitiesCost;

    //endregion
    //region Economic
    private Double economicAveragePrice;
    private Long economicClientsAmt;
    private Long economicOrdersCnt;
    private Double economicClientsOutflowPercent;
    private Double economicProceedPercent;
    private Double economicClientAttractionCost;
    //endregion
    //region Investments
    private Double investmentsAmount;
    private Double investmentsFinancingCostPercent;
    private Double investmentsShowingCost;
    private Double investmentsIncomeTaxPercent;
    private Double investmentsConversionToApplicationsPercent;
    private Integer investmentsCustomerGrowth;
    private Double investmentsCustomerCost;
    private Double investmentsMonthGrowth;
    private Double investmentsRequestsToPurchasesConversionPercent;
    private Double investmentsCustomerServiceCost;
    //endregion
}
