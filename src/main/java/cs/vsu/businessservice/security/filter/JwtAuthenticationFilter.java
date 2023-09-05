package cs.vsu.businessservice.security.filter;

import cs.vsu.businessservice.exceptionhandling.exception.ErrorMessage;
import cs.vsu.businessservice.security.misc.TokenType;
import cs.vsu.businessservice.security.service.HttpServletUtilsService;
import cs.vsu.businessservice.security.service.JwtService;
import cs.vsu.businessservice.user.repo.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepo userRepo;
    private final HttpServletUtilsService httpServletUtilsService;

    private static final String AUTH_HEADER_START_WITH = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final String username;
        try {
            if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_START_WITH)) {
                filterChain.doFilter(request, response);
                return;
            }
            jwtToken = authHeader.substring(AUTH_HEADER_START_WITH.length());

            username = jwtService.extractUsername(jwtToken, TokenType.ACCESS);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwtToken, userDetails, TokenType.ACCESS)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            var description = new StringBuilder();
            var exceptionResponseMapper = httpServletUtilsService.modifyResponseContentType(request, response, description);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try {
                response.getWriter().write(
                        exceptionResponseMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                                ErrorMessage.builder()
                                        .message(e.getMessage())
                                        .description(description.toString())
                                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                                        .time(LocalDateTime.now())
                                        .build()
                        )
                );
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
                exceptionResponseMapper = httpServletUtilsService.modifyResponseContentType(request, response, description);
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write(
                        exceptionResponseMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                                ErrorMessage.builder()
                                        .message(ex.getMessage())
                                        .description(description.toString())
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .time(LocalDateTime.now())
                                        .build()
                        )
                );
            }

        }

    }
}
