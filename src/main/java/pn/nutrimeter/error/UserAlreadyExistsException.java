package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User already exists!")
public class UserAlreadyExistsException extends BaseRuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
