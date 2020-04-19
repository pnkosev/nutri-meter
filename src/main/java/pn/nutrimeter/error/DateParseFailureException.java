package pn.nutrimeter.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.ParseException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot parse date!")
public class DateParseFailureException extends ParseException {

    /**
     * Constructs a ParseException with the specified detail message and
     * offset.
     * A detail message is a String that describes this particular exception.
     *
     * @param s           the detail message
     * @param errorOffset the position where the error is found while parsing.
     */
    public DateParseFailureException(String s, int errorOffset) {
        super(s, errorOffset);
    }
}
