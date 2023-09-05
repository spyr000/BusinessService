package cs.vsu.businessservice.project.controller;

import com.google.gson.Gson;
import cs.vsu.businessservice.project.dto.ProjectRequest;
import cs.vsu.businessservice.project.dto.ProjectResponse;
import cs.vsu.businessservice.project.entity.Project;
import cs.vsu.businessservice.project.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final Gson gson;
    @PostMapping(
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<ProjectResponse> add(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody ProjectRequest projectRequest
    ) throws URISyntaxException {
        var project = projectService.add(authHeader, projectRequest);
        return ResponseEntity
                .created(new URI("/api/v1/projects/" + project.getId()))
                .body(ProjectResponse.fromProject(project));
    }

    @GetMapping(
            value="/{id}",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<Project> get(
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var project = projectService.getProject(authHeader, projectId);
        return ResponseEntity.ok()
                .body(project);
    }

//    @PutMapping("/{id}/edit")
//    public ResponseEntity<?> edit(
//            @RequestBody ProjectRequest request,
//            @PathVariable(name = "id") Long projectId,
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
//        var project = projectService.editProject(authHeader, projectId, request);
//        return ResponseEntity.ok()
//                .body(ProjectResponse.fromProject(project));
//    }
    @PutMapping(
            value ="/{id}/edit",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<?> edit(
            @RequestBody String json,
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        var request = gson.fromJson(json, Project.class);
        var project = projectService.editProject(authHeader, projectId, request);
        return ResponseEntity.ok()
                .body(ProjectResponse.fromProject(project));
    }

    @GetMapping(
            value ="/{id}/results",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public void getResults(
            @PathVariable(name = "id") Long projectId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            HttpServletResponse response
    ) {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        projectService.getResults(authHeader, projectId, response);
    }

    @DeleteMapping(
            value ="/{id}/delete",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<?> delete(
            @PathVariable(name = "id") Long projectId,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        projectService.deleteProject(authHeader,projectId);
        return ResponseEntity.ok().build();
    }
}
