package cs.vsu.businessservice.project.service.impl;
import cs.vsu.businessservice.project.dto.ProjectRequest;
import cs.vsu.businessservice.security.misc.TokenType;
import cs.vsu.businessservice.user.entity.User;
import cs.vsu.businessservice.exceptionhandling.exception.BaseException;
import cs.vsu.businessservice.exceptionhandling.exception.EntityNotFoundException;
import cs.vsu.businessservice.exceptionhandling.exception.UnableToAccessToForeignProjectException;
import cs.vsu.businessservice.project.entity.*;
import cs.vsu.businessservice.project.repo.ProjectRepo;
import cs.vsu.businessservice.project.service.ProjectService;
import cs.vsu.businessservice.project.service.ReflectionService;
import cs.vsu.businessservice.project.result.service.PdfService;
import cs.vsu.businessservice.user.service.*;
import cs.vsu.businessservice.security.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepo projectRepo;
    private final JwtService jwtService;
    private final UserService userService;
    private final ReflectionService reflectionService;
    private final PdfService pdfService;

//    public void checkHeader(String authHeader) {
//        if (!jwtService.isAuthHeaderSuitable(authHeader)) {
//            throw new NoAuthHeaderException(HttpStatus.UNAUTHORIZED,
//                    "No authentication header in request"
//            );
//        }
//    }

    public Project getProjectIfAccessible(String authHeader, long projectId) {
//        checkHeader(authHeader);
        var project = getProject(projectId);
        if (!jwtService.isUserHaveAccessToProject(authHeader, project)) {
            throw new UnableToAccessToForeignProjectException(
                    HttpStatus.UNAUTHORIZED,
                    "You do not have access to this project"
            );
        }
        return project;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Project add(String authHeader, ProjectRequest projectRequest) {
//        checkHeader(authHeader);
        var jwtToken = authHeader.substring(7);
        var user = userService.getUser(jwtService.extractUsername(jwtToken, TokenType.ACCESS));
        var project = Project.builder()
                .name(projectRequest.getProjectName())
                .desc(projectRequest.getProjectDesc())
                .yearsCount(projectRequest.getProjectYearsCount())
                .lastEditingTime(LocalDateTime.now())
                .user(user)
                .build();
        var economic = Economic.builder()
                .averagePrice(projectRequest.getEconomicAveragePrice())
                .clientAttractionCost(projectRequest.getEconomicClientAttractionCost())
                .clientsAmt(projectRequest.getEconomicClientsAmt())
                .proceedPercent(projectRequest.getEconomicProceedPercent())
                .ordersCnt(projectRequest.getEconomicOrdersCnt())
                .clientsOutflowPercent(projectRequest.getEconomicClientsOutflowPercent())
                .build();
        var fixedExpenses = FixedExpenses.builder()
                .officeRentalCost(projectRequest.getFixedExpensesOfficeRentalCost())
                .incomeTaxPercent(projectRequest.getFixedExpensesIncomeTaxPercent())
                .insuranceCost(projectRequest.getFixedExpensesInsuranceCost())
                .marketingCost(projectRequest.getFixedExpensesMarketingCost())
                .equipmentServiceCost(projectRequest.getFixedExpensesEquipmentServiceCost())
                .publicUtilitiesCost(projectRequest.getFixedExpensesPublicUtilitiesCost())
                .wageFundCost(projectRequest.getFixedExpensesWageFundCost())
                .build();
        var variableExpenses = VariableExpenses.builder()
                .otherExpensesCost(projectRequest.getVariableExpensesOtherExpensesCost())
                .logisticsCost(projectRequest.getVariableExpensesLogisticsCost())
                .officeToolsCost(projectRequest.getVariableExpensesOfficeToolsCost())
                .equipmentCost(projectRequest.getVariableExpensesEquipmentCost())
                .build();
        var investments = Investments.builder()
                .amount(projectRequest.getInvestmentsAmount())
                .financingCostPercent(projectRequest.getInvestmentsFinancingCostPercent())
                .showingCost(projectRequest.getInvestmentsShowingCost())
                .clickConversionPercent(projectRequest.getInvestmentsClickConversionPercent())
                .conversionToApplicationsPercent(projectRequest.getInvestmentsConversionToApplicationsPercent())
                .customerGrowth(projectRequest.getInvestmentsCustomerGrowth())
                .requestsToPurchasesConversionPercent(projectRequest.getInvestmentsRequestsToPurchasesConversionPercent())
                .customerCost(projectRequest.getInvestmentsCustomerCost())
                .monthGrowth(projectRequest.getInvestmentsMonthGrowth())
                .customerServiceCost(projectRequest.getInvestmentsCustomerServiceCost())
                .build();
        project.setEconomic(economic);
        project.setFixedExpenses(fixedExpenses);
        project.setVariableExpenses(variableExpenses);
        project.setInvestments(investments);
        projectRepo.save(project);
        return project;
    }

    @Override
    public Project getProject(Long id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Project.class));
    }

    @Override
    public Set<Project> getAllUserProjects(User user) {
        return projectRepo.findByUserId(user.getId());
    }

    @Override
    public boolean isProjectNotAccessible(String authHeader, User repoUser) {
        var jwtToken = authHeader.substring(7);
        return !jwtService.isTokenValid(jwtToken, repoUser, TokenType.ACCESS);
    }

    @Override
    public Project getProject(String authHeader, Long id) {
//        if (!jwtService.isAuthHeaderSuitable(authHeader)) {
//            throw new NoAuthHeaderException(HttpStatus.UNAUTHORIZED, "No authentication header in request");
//        }

        var project = getProject(id);
        var username = project.getUser().getUsername();
        var repoUser = userService.getUser(username);
        if (isProjectNotAccessible(authHeader, repoUser)) {
            throw new UnableToAccessToForeignProjectException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to get access to foreign project");
        }
        return project;
    }

    @Override
    public Project editProject(String authHeader, long projectId, Project request) {
        var project = getProjectIfAccessible(authHeader, projectId);
        var projectFixedExpenses = project.getFixedExpenses();
        var projectVariableExpenses = project.getVariableExpenses();
        var projectEconomic = project.getEconomic();
        var projectInvestments = project.getInvestments();
        var description = request.getDesc();
        var name = request.getName();
        var yearsCount = request.getYearsCount();
        var fixedExpenses = request.getFixedExpenses();
        var fixedExpensesEquipmentServiceCost = fixedExpenses.getEquipmentServiceCost();
        var fixedExpensesMarketingCost = fixedExpenses.getMarketingCost();
        var fixedExpensesInsuranceCost = fixedExpenses.getInsuranceCost();
        var fixedExpensesOfficeRentalCost = fixedExpenses.getOfficeRentalCost();
        var fixedExpensesPublicUtilitiesCost = fixedExpenses.getPublicUtilitiesCost();
        var fixedExpensesWageFundCost = fixedExpenses.getWageFundCost();
        var fixedExpensesIncomeTaxPercent = fixedExpenses.getIncomeTaxPercent();
        var variableExpenses = request.getVariableExpenses();
        var variableExpensesEquipmentCost = variableExpenses.getEquipmentCost();
        var variableExpensesLogisticsCost = variableExpenses.getLogisticsCost();
        var variableExpensesOfficeToolsCost = variableExpenses.getOfficeToolsCost();
        var variableExpensesOtherExpensesCost = variableExpenses.getOtherExpensesCost();
        var economic = request.getEconomic();
        var economicAvgPrice = economic.getAveragePrice();
        var economicClientsAttractionCost = economic.getClientAttractionCost();
        var economicClientsAmt = economic.getClientsAmt();
        var economicOutflowPercent = economic.getClientsOutflowPercent();
        var economicOrdersCnt = economic.getOrdersCnt();
        var economicProceedPercent = economic.getProceedPercent();
        var investments = request.getInvestments();
        var investmentsAmount = investments.getAmount();
        var investmentsConversionToApplicationPercent = investments.getConversionToApplicationsPercent();
        var investmentsCustomerCost = investments.getCustomerCost();
        var investmentsCustomerGrowth = investments.getCustomerGrowth();
        var investmentsCustomerServiceCost = investments.getCustomerServiceCost();
        var investmentsFinancingCostPercent = investments.getFinancingCostPercent();
        var investmentsClickConversionPercent = investments.getClickConversionPercent();
        var investmentsMonthGrowth = investments.getMonthGrowth();
        var investmentsRequestsToPurchasesConversionPercent = investments.getRequestsToPurchasesConversionPercent();
        var investmentsShowingCost = investments.getShowingCost();
//        investments.getIncomeTaxPercent();

        Optional.ofNullable(fixedExpensesEquipmentServiceCost).ifPresent(projectFixedExpenses::setEquipmentServiceCost);
        Optional.ofNullable(fixedExpensesMarketingCost).ifPresent(projectFixedExpenses::setMarketingCost);
        Optional.ofNullable(fixedExpensesInsuranceCost).ifPresent(projectFixedExpenses::setInsuranceCost);
        Optional.ofNullable(fixedExpensesOfficeRentalCost).ifPresent(projectFixedExpenses::setOfficeRentalCost);
        Optional.ofNullable(fixedExpensesPublicUtilitiesCost).ifPresent(projectFixedExpenses::setPublicUtilitiesCost);
        Optional.ofNullable(fixedExpensesWageFundCost).ifPresent(projectFixedExpenses::setWageFundCost);
        Optional.ofNullable(fixedExpensesIncomeTaxPercent).ifPresent(projectFixedExpenses::setIncomeTaxPercent);
        Optional.ofNullable(economicAvgPrice).ifPresent(projectEconomic::setAveragePrice);
        Optional.ofNullable(economicClientsAttractionCost).ifPresent(projectEconomic::setClientAttractionCost);
        Optional.ofNullable(economicClientsAmt).ifPresent(projectEconomic::setClientsAmt);
        Optional.ofNullable(economicOutflowPercent).ifPresent(projectEconomic::setClientsOutflowPercent);
        Optional.ofNullable(economicOrdersCnt).ifPresent(projectEconomic::setOrdersCnt);
        Optional.ofNullable(economicProceedPercent).ifPresent(projectEconomic::setProceedPercent);
        Optional.ofNullable(variableExpensesLogisticsCost).ifPresent(projectVariableExpenses::setLogisticsCost);
        Optional.ofNullable(variableExpensesOfficeToolsCost).ifPresent(projectVariableExpenses::setOfficeToolsCost);
        Optional.ofNullable(variableExpensesEquipmentCost).ifPresent(projectVariableExpenses::setEquipmentCost);
        Optional.ofNullable(variableExpensesOtherExpensesCost).ifPresent(projectVariableExpenses::setOtherExpensesCost);
        Optional.ofNullable(investmentsClickConversionPercent).ifPresent(projectInvestments::setClickConversionPercent);
        Optional.ofNullable(investmentsAmount).ifPresent(projectInvestments::setAmount);
        Optional.ofNullable(investmentsCustomerCost).ifPresent(projectInvestments::setCustomerCost);
        Optional.ofNullable(investmentsConversionToApplicationPercent).ifPresent(projectInvestments::setConversionToApplicationsPercent);
        Optional.ofNullable(investmentsCustomerServiceCost).ifPresent(projectInvestments::setCustomerServiceCost);
        Optional.ofNullable(investmentsCustomerGrowth).ifPresent(projectInvestments::setCustomerGrowth);
        Optional.ofNullable(investmentsFinancingCostPercent).ifPresent(projectInvestments::setFinancingCostPercent);
        Optional.ofNullable(investmentsMonthGrowth).ifPresent(projectInvestments::setMonthGrowth);
        Optional.ofNullable(investmentsRequestsToPurchasesConversionPercent).ifPresent(projectInvestments::setRequestsToPurchasesConversionPercent);
        Optional.ofNullable(investmentsShowingCost).ifPresent(projectInvestments::setShowingCost);

//        projectInvestments.setProject(project);
////        projectInvestments.setId(investments.getId());
//        projectVariableExpenses.setProject(project);
////        projectVariableExpenses.setId(variableExpenses.getId());
//        projectFixedExpenses.setProject(project);
////        projectFixedExpenses.setId(fixedExpenses.getId());
//        projectEconomic.setProject(project);
////        projectEconomic.setId(economic.getId());
//        project.setUser(request.getUser());
//        project.setId(request.getId());
//        project.setEconomic(projectEconomic);
//        project.setInvestments(projectInvestments);
//        project.setFixedExpenses(projectFixedExpenses);
//        project.setVariableExpenses(projectVariableExpenses);

        project.setLastEditingTime(LocalDateTime.now());

//        Optional.ofNullable(description).ifPresent(project::setDesc);
//        Optional.ofNullable(name).ifPresent(project::setName);
//        Optional.ofNullable(yearsCount).ifPresent(project::setYearsCount);
        projectRepo.save(project);
        return project;
    }

    @Override
    public void getResults(String authHeader, long projectId, HttpServletResponse response) {
        var project = getProjectIfAccessible(authHeader, projectId);
        pdfService.makeResultsPdf(project, response);
    }

    @Override
    public void deleteProject(String authHeader, long projectId) {
        try {
            projectRepo.deleteById(projectId);
        } catch (Exception e) {
            throw new BaseException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}
