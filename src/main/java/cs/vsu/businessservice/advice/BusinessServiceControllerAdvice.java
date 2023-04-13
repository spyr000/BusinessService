package cs.vsu.businessservice.advice;

import cs.vsu.businessservice.exception.BaseException;
import cs.vsu.businessservice.exception.ExceptionResponseConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SignatureException;

@ControllerAdvice
public class BusinessServiceControllerAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleException(BaseException e) {
        LoggerFactory.getLogger(this.getClass()).error(e.getDescription(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(e.getDescription());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleException(
            UsernameNotFoundException e
    ) {
        LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleException(
            SignatureException e
    ) {
        LoggerFactory
                .getLogger(this.getClass())
                .error(ExceptionResponseConstants.SIGNATURE_EXCEPTION_RESPONSE, e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponseConstants.SIGNATURE_EXCEPTION_RESPONSE);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleException(
            ExpiredJwtException e
    ) {
        LoggerFactory
                .getLogger(this.getClass())
                .error(ExceptionResponseConstants.TOKEN_EXPIRED_EXCEPTION_RESPONSE, e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponseConstants.TOKEN_EXPIRED_EXCEPTION_RESPONSE);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleException(
            MalformedJwtException e
    ) {
        LoggerFactory
                .getLogger(this.getClass())
                .error(ExceptionResponseConstants.MALFORMED_JWT_EXCEPTION_RESPONSE, e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponseConstants.MALFORMED_JWT_EXCEPTION_RESPONSE);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleException(
            BadCredentialsException e
    ) {
        LoggerFactory
                .getLogger(this.getClass())
                .error(ExceptionResponseConstants.BAD_CREDENTIALS_EXCEPTION_RESPONSE, e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponseConstants.BAD_CREDENTIALS_EXCEPTION_RESPONSE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(
            HttpMessageNotReadableException e
    ) {
        LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable e) {
        LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
        return ResponseEntity
                .internalServerError()
                .body(e.getMessage());
    }
}

