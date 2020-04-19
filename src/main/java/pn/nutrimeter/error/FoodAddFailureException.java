package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred upon food adding!")
public class FoodAddFailureException extends RuntimeException {

    public FoodAddFailureException(String message) { super(message); }
}
