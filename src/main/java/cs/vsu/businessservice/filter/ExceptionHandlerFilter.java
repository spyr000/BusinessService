package cs.vsu.businessservice.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs.vsu.businessservice.exception.InvalidTokenException;
import cs.vsu.businessservice.exception.UnableToWriteExceptionMessageToJsonException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            var errorResponse = new InvalidTokenException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(convertObjectToJson(errorResponse));
        }
    }

    public String convertObjectToJson(Object object) {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UnableToWriteExceptionMessageToJsonException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
            );
        }
    }
}