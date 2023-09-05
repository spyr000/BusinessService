package cs.vsu.businessservice.project.service;

import cs.vsu.businessservice.project.dto.ProjectRequest;
import cs.vsu.businessservice.project.entity.Project;
import cs.vsu.businessservice.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;

public interface ProjectService {
    Project add(String authHeader, ProjectRequest projectRequest);

    Project getProject(Long id);

    Set<Project> getAllUserProjects(User user);

    boolean isProjectNotAccessible(String authHeader, User repoUser);

    Project getProject(String authHeader, Long id);
//    Project editProject(String authHeader, long projectId, ProjectRequest request);

    Project editProject(String authHeader, long projectId, Project request);

    void getResults(String authHeader, long projectId, HttpServletResponse response);

    void deleteProject(String authHeader, long projectId);
}
