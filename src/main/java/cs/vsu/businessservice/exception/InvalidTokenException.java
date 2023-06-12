package cs.vsu.businessservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(HttpStatus httpStatus, String description) {
        super(httpStatus, description);
    }
}
