package cs.vsu.businessservice.service.security.impl;

import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.service.UserService;
import cs.vsu.businessservice.service.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@Setter
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final Environment env;

    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + Integer.parseInt(
                                        Objects.requireNonNull(
                                                env.getProperty("security.encryption.token-expiration-time-millis")
                                        )
                                )
                        )
                )
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean isUserHaveAccessToProject(String authHeader, Project project) {
        var tokenStartIndex = 7;
        var token = authHeader.substring(tokenStartIndex);
        var tokenUsername = extractUsername(token);
        var dbUsername = project.getUser().getUsername();
        return tokenUsername.equals(dbUsername);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsExtractor) {
        final Claims claims = extractAllClaims(token);
        return claimsExtractor.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("security.encryption.key"));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean isAuthHeaderSuitable(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }
}
