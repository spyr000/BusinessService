package cs.vsu.businessservice.service.impl;

import cs.vsu.businessservice.dto.project.ProjectRequest;
import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.entity.User;
import cs.vsu.businessservice.exception.EntityNotFoundException;
import cs.vsu.businessservice.exception.NoAuthHeaderException;
import cs.vsu.businessservice.exception.UnableToAccessToForeignProjectException;
import cs.vsu.businessservice.repo.ProjectRepo;
import cs.vsu.businessservice.service.ProjectService;
import cs.vsu.businessservice.service.ReflectionService;
import cs.vsu.businessservice.service.UserService;
import cs.vsu.businessservice.service.security.JwtService;
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

    @Override
    public Project add(ProjectRequest projectRequest) {
        var user = userService.getUser(projectRequest.getUserId());
        return Project.builder()
                .name(projectRequest.getProjectName())
                .creationTime(LocalDateTime.now())
                .user(user)
                .build();
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
        var userId = project.getUser().getId();
        var repoUser = userService.getUser(userId);
        if (isProjectNotAccessible(authHeader, repoUser)) {
            throw new UnableToAccessToForeignProjectException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to get access to foreign project");
        }
        return project;
    }

    @Override
    public Project editProject(Project project, ProjectRequest request) {
        return reflectionService.modifyEntity(project,
                request,
                "project",
                new String[]{}
        );
    }
}
