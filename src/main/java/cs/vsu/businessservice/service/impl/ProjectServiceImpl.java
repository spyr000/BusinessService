package cs.vsu.businessservice.service.impl;
import cs.vsu.businessservice.dto.project.ProjectRequest;
import cs.vsu.businessservice.entity.*;
import cs.vsu.businessservice.exception.EntityNotFoundException;
import cs.vsu.businessservice.exception.NoAuthHeaderException;
import cs.vsu.businessservice.exception.UnableToAccessToForeignProjectException;
import cs.vsu.businessservice.repo.ProjectRepo;
import cs.vsu.businessservice.service.*;
import cs.vsu.businessservice.service.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepo projectRepo;
    private final JwtService jwtService;
    private final UserService userService;
    private final ReflectionService reflectionService;
    private final PdfService pdfService;

    public void checkHeader(String authHeader) {
        if (!jwtService.isAuthHeaderSuitable(authHeader)) {
            throw new NoAuthHeaderException(HttpStatus.UNAUTHORIZED,
                    "No authentication header in request"
            );
        }
    }

    public Project getProjectIfAccessible(String authHeader, long projectId) {
        checkHeader(authHeader);
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
        checkHeader(authHeader);
        var jwtToken = authHeader.substring(7);
        var user = userService.getUser(jwtService.extractUsername(jwtToken));
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
                .incomeTaxPercent(projectRequest.getInvestmentsIncomeTaxPercent())
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
        return !jwtService.isTokenValid(jwtToken, repoUser);
    }

    @Override
    public Project getProject(String authHeader, Long id) {
        if (!jwtService.isAuthHeaderSuitable(authHeader)) {
            throw new NoAuthHeaderException(HttpStatus.UNAUTHORIZED, "No authentication header in request");
        }

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
    public Project editProject(String authHeader, long projectId, ProjectRequest request) {
        var project = getProjectIfAccessible(authHeader, projectId);
        var modifiedProject = reflectionService.modifyEntity(
                project,
                request,
                new String[]{"Id"}
        );
        projectRepo.save(
                modifiedProject
        );

        return modifiedProject;
    }

    @Override
    public void getResults(String authHeader, long projectId, HttpServletResponse response) {
        var project = getProjectIfAccessible(authHeader, projectId);
        pdfService.makeResultsPdf(project, response);
    }
}
