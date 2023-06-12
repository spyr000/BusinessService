package cs.vsu.businessservice.exception;

import org.springframework.http.HttpStatus;

public class UnableToWriteExceptionMessageToJsonException extends BaseException {
    public UnableToWriteExceptionMessageToJsonException(HttpStatus httpStatus, String description) {
        super(httpStatus, description);
    }
}
