package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.format.DateTimeParseException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot parse date!")
public class DateParseFailureException extends DateTimeParseException {
    private HttpStatus httpStatus;

    public DateParseFailureException(String message, CharSequence parsedData, int errorIndex) {
        super(message, parsedData, errorIndex);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
