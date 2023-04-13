package cs.vsu.businessservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnableToAccessToForeignProjectException extends BaseException {
    public UnableToAccessToForeignProjectException(HttpStatus httpStatus, String description) {
        super(httpStatus, description);
    }
}
