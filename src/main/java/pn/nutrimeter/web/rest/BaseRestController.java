package pn.nutrimeter.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import pn.nutrimeter.error.BaseRuntimeException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RequestMapping("/api")
public class BaseRestController {
    protected BaseRestController() {}

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<String> handleCustomException(BaseRuntimeException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    /**
     * Handling validation exceptions
     * @param ex from class MethodArgumentNotValidException
     * @return ResponseEntity<Map<String, String>>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handling list validation exceptions
     * @param ex from class ConstraintViolationException
     * @return ResponseEntity<Map<String, String>>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleListValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(v -> {
                    String fieldName = null;
                    for (Path.Node node : v.getPropertyPath()) {
                        fieldName = node.getName();
                    }
                    String errorMessage = v.getMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
