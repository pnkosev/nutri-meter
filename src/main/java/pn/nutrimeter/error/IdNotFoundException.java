package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such id found!")
public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException(String message) { super(message); }
}
