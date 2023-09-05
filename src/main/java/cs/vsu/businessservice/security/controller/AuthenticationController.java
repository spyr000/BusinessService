package cs.vsu.businessservice.security.controller;


import cs.vsu.businessservice.security.dto.AuthenticationRequest;
import cs.vsu.businessservice.security.dto.AuthenticationResponse;
import cs.vsu.businessservice.security.dto.RefreshJwtRequest;
import cs.vsu.businessservice.security.dto.RegistrationRequest;
import cs.vsu.businessservice.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Transactional(rollbackOn = Exception.class)
    @PostMapping(
            value = "/register",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping(
            value ="/authenticate",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping(
            value ="/token",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<AuthenticationResponse> token(
            @RequestBody RefreshJwtRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request, false));
    }

    @GetMapping(
            value ="/refresh",
            produces = { "application/json", "application/xml", "application/x-yaml" }
    )
    public ResponseEntity<AuthenticationResponse> refresh(
            @RequestBody RefreshJwtRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request, true));
    }
}
