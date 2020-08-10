package pn.nutrimeter.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import pn.nutrimeter.error.BaseRuntimeException;


@RequestMapping("/api")
public class BaseRestController {
    protected BaseRestController() {}

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<String> handleCustomException(BaseRuntimeException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
