package cs.vsu.businessservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import cs.vsu.businessservice.exception.ErrorMessage;
import cs.vsu.businessservice.service.security.impl.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserDetailsServiceImpl userDetailService;

    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.error(authException.getMessage(), authException);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getOutputStream().println(
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                            ErrorMessage.builder()
                                    .message(authException.getMessage())
                                    .description(request.getRequestURI())
                                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                                    .time(LocalDateTime.now())
                                    .build()
                    )
            );
        };
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            log.error(accessDeniedException.getMessage(), accessDeniedException);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getOutputStream().println(
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                            ErrorMessage.builder()
                                    .message(accessDeniedException.getMessage())
                                    .description(request.getRequestURI())
                                    .statusCode(HttpStatus.FORBIDDEN.value())
                                    .time(LocalDateTime.now())
                                    .build()
                    ));
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    JsonDeserializer<LocalDateTime> jsonDeserializer() {
        return (jsonElement, type, jsonDeserializationContext) -> LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
    }

    @Bean
    public Gson gson(JsonDeserializer<LocalDateTime> jsonDeserializer) {
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, jsonDeserializer);

        return gsonBuilder.setPrettyPrinting().create();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
