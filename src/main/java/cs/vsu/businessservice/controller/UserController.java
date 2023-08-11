package cs.vsu.businessservice.controller;


import cs.vsu.businessservice.service.ProjectService;
import cs.vsu.businessservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ProjectService projectService;

    @GetMapping(
            value = "/{username}/projects",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<?> getUserProjects(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable(name = "username") String username
    ) {

        var user = userService.getUser(username);

        return ResponseEntity.ok()
                .body(projectService.getAllUserProjects(user));
    }
}
