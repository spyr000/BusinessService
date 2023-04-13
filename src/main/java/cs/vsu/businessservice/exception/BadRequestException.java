package cs.vsu.businessservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseException {
    public BadRequestException(Class<?> clazz) {
        httpStatus = HttpStatus.BAD_REQUEST;
        description = "Received request is malformed";
    }
}
