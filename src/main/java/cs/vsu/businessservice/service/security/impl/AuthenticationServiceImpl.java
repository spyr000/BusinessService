package cs.vsu.businessservice.service.security.impl;


import cs.vsu.businessservice.exception.BaseException;
import cs.vsu.businessservice.dto.security.AuthenticationRequest;
import cs.vsu.businessservice.dto.security.AuthenticationResponse;
import cs.vsu.businessservice.dto.security.RegistrationRequest;
import cs.vsu.businessservice.entity.Role;
import cs.vsu.businessservice.entity.User;
import cs.vsu.businessservice.repo.UserRepo;
import cs.vsu.businessservice.service.security.AuthenticationService;
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

    private final JwtServiceImpl jwtServiceImpl;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegistrationRequest request) {
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getUsername(),
                Role.USER
        );
        userRepo.save(user);
        String token = jwtServiceImpl.generateToken(user);

        return new AuthenticationResponse(token);
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

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtServiceImpl.generateToken(user);
        return new AuthenticationResponse(token);
    }
}
