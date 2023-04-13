package cs.vsu.businessservice.service.security;

import cs.vsu.businessservice.dto.security.AuthenticationRequest;
import cs.vsu.businessservice.dto.security.AuthenticationResponse;
import cs.vsu.businessservice.dto.security.RegistrationRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
