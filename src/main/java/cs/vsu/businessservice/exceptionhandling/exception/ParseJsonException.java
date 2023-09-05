package cs.vsu.businessservice.exceptionhandling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ParseJsonException extends BaseException {
    public ParseJsonException(HttpStatus httpStatus, String description) {
        super(httpStatus, description);
    }
}

