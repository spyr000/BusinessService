package cs.vsu.businessservice.service.security;

import cs.vsu.businessservice.entity.Project;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isUserHaveAccessToProject(String authHeader, Project project);

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsExtractor);

    boolean isAuthHeaderSuitable(String authHeader);
}
