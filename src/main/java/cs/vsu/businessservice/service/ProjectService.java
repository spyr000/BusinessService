package cs.vsu.businessservice.service;

import cs.vsu.businessservice.dto.project.ProjectRequest;
import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.entity.User;

import java.util.Set;

public interface ProjectService {
    Project add(ProjectRequest projectRequest);

    Project getProject(Long id);

    Set<Project> getAllUserProjects(User user);

    boolean isProjectNotAccessible(String authHeader, User repoUser);

    Project getProject(String authHeader, Long id);


    Project editProject(Project project, ProjectRequest request);
}
