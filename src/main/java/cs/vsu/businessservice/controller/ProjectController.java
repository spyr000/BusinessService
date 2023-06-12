package cs.vsu.businessservice.controller;

import cs.vsu.businessservice.dto.project.ProjectRequest;
import cs.vsu.businessservice.dto.project.ProjectResponse;
import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping
    public ResponseEntity<ProjectResponse> add(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody ProjectRequest projectRequest
    ) throws URISyntaxException {
        var project = projectService.add(authHeader, projectRequest);
        return ResponseEntity
                .created(new URI("/api/v1/projects/" + project.getId()))
                .body(ProjectResponse.fromProject(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var project = projectService.getProject(authHeader, projectId);
        return ResponseEntity.ok()
                .body(project);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> edit(
            @RequestBody ProjectRequest request,
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var project = projectService.editProject(authHeader, projectId, request);
        return ResponseEntity.ok()
                .body(ProjectResponse.fromProject(project));
    }

    @GetMapping("/{id}/results")
    public void getResults(
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            HttpServletResponse response
    ) {
        System.out.println(projectId);
        System.out.println(authHeader);
        System.out.println(response);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        projectService.getResults(authHeader, projectId, response);
    }
}

