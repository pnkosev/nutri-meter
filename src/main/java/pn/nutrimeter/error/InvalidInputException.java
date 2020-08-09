package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid input!")
public class InvalidInputException extends BaseRuntimeException {

    public InvalidInputException(String message) {
        super(message);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
