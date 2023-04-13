package cs.vsu.businessservice.controller;


import cs.vsu.businessservice.entity.User;
import cs.vsu.businessservice.repo.UserRepo;
import cs.vsu.businessservice.service.ProjectService;
import cs.vsu.businessservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ProjectService projectService;

    @GetMapping("/{user_id}/projects")
    public ResponseEntity<?> getUserProjects(@PathVariable(name = "user_id") Long userId) {
        var user = userService.getUser(userId);

        return ResponseEntity.ok()
                .body(projectService.getAllUserProjects(user));
    }
}
