package cs.vsu.businessservice.controller;

import cs.vsu.businessservice.dto.project.ProjectRequest;
import cs.vsu.businessservice.dto.project.ProjectResponse;
import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.exception.NoAuthHeaderException;
import cs.vsu.businessservice.exception.UnableToAccessToForeignProjectException;
import cs.vsu.businessservice.service.ProjectService;
import cs.vsu.businessservice.service.ReflectionService;
import cs.vsu.businessservice.service.UserService;
import cs.vsu.businessservice.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping
    public ResponseEntity<ProjectResponse> add(
            @RequestBody ProjectRequest projectRequest
    ) throws URISyntaxException {

        var project = projectService.add(projectRequest);

        return ResponseEntity
                .created(new URI("/api/v1/projects/" + project.getId()))
                .body(ProjectResponse.fromProject(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> get(
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var project = projectService.getProject(authHeader, projectId);

        return ResponseEntity.ok()
                .body(ProjectResponse.fromProject(project));
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> edit(
            @RequestBody ProjectRequest request,
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var project = projectService.getProject(authHeader, projectId);
        project = projectService.editProject(project,request);
        return ResponseEntity.ok()
                .body(ProjectResponse.fromProject(project));
    }

}

