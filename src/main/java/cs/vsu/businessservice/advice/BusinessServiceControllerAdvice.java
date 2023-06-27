package cs.vsu.businessservice.advice;

import cs.vsu.businessservice.exception.BaseException;
import cs.vsu.businessservice.exception.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.security.SignatureException;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class BusinessServiceControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorMessage> handleException(
            BaseException e, WebRequest request
    ) {
        log.error(e.getDescription(), e);
        var message = ErrorMessage.builder()
                .message(e.getDescription())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(message);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(
            UsernameNotFoundException e, WebRequest request
    ) {
        log.error(e.getMessage(), e);
        var message = ErrorMessage.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorMessage> handleException(
            SignatureException e, WebRequest request
    ) {
        log.error(e.getMessage(), e);
        var message = ErrorMessage.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> handleException(
            ExpiredJwtException e, WebRequest request
    ) {
        log.error(e.getMessage(), e);
        var message = ErrorMessage.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorMessage> handleException(
            MalformedJwtException e, WebRequest request
    ) {
        log.error(e.getMessage(), e);
        var message = ErrorMessage.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleException(
            BadCredentialsException e, WebRequest request
    ) {
        log.error(e.getMessage(), e);
        var message = ErrorMessage.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleException(
            HttpMessageNotReadableException e, WebRequest request
    ) {
        log.error(e.getMessage(), e);
        var message = ErrorMessage.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .internalServerError()
                .body(e.getMessage());
    }
}

