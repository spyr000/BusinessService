package cs.vsu.businessservice.security.service.impl;


import cs.vsu.businessservice.exceptionhandling.exception.BaseException;
import cs.vsu.businessservice.exceptionhandling.exception.ExpiredRefreshTokenException;
import cs.vsu.businessservice.security.dto.AuthenticationRequest;
import cs.vsu.businessservice.security.dto.AuthenticationResponse;
import cs.vsu.businessservice.security.dto.RefreshJwtRequest;
import cs.vsu.businessservice.security.dto.RegistrationRequest;
import cs.vsu.businessservice.security.misc.TokenType;
import cs.vsu.businessservice.security.service.JwtService;
import cs.vsu.businessservice.user.entity.Role;
import cs.vsu.businessservice.user.entity.User;
import cs.vsu.businessservice.user.repo.UserRepo;
import cs.vsu.businessservice.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse register(RegistrationRequest request) {
        var user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getUsername(),
                Role.USER
        );
        userRepo.save(user);
        var accessToken = jwtService.generateToken(user, TokenType.ACCESS);
        var refreshToken = jwtService.generateToken(user, TokenType.REFRESH);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new BaseException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var accessToken = jwtService.generateToken(user, TokenType.ACCESS);
        var refreshToken = jwtService.generateToken(user, TokenType.REFRESH);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshJwtRequest request, boolean refreshTokenNeeded) {
        var refreshToken = request.getRefreshToken();
        var user = userRepo.findByUsername(
                jwtService.extractUsername(refreshToken, TokenType.REFRESH)
        ).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user, TokenType.REFRESH)) {
            throw new ExpiredRefreshTokenException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }
        return new AuthenticationResponse(
                jwtService.generateToken(user, TokenType.ACCESS),
                refreshTokenNeeded ? jwtService.generateToken(user, TokenType.REFRESH) : refreshToken
        );
    }

}
