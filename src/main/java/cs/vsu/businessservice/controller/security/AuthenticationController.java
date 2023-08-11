package cs.vsu.businessservice.controller.security;


import cs.vsu.businessservice.dto.security.AuthenticationRequest;
import cs.vsu.businessservice.dto.security.AuthenticationResponse;
import cs.vsu.businessservice.dto.security.RegistrationRequest;
import cs.vsu.businessservice.service.security.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @Transactional(rollbackOn = Exception.class)
    @GetMapping(
            value = "/register",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping(
            value ="/authenticate",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<AuthenticationResponse> authenticateRequest(
            @RequestBody AuthenticationRequest request
    ) {

        return ResponseEntity.ok(service.authenticate(request));
    }
}
