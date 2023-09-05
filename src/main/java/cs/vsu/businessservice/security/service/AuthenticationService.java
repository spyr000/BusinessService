package cs.vsu.businessservice.security.service;

import cs.vsu.businessservice.security.dto.AuthenticationRequest;
import cs.vsu.businessservice.security.dto.AuthenticationResponse;
import cs.vsu.businessservice.security.dto.RefreshJwtRequest;
import cs.vsu.businessservice.security.dto.RegistrationRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshJwtRequest request, boolean refreshTokenNeeded);
}
