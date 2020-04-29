package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;

public class BaseRuntimeException extends RuntimeException {
    protected HttpStatus httpStatus;

    protected BaseRuntimeException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
