package pn.nutrimeter.web.controllers;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;
import pn.nutrimeter.error.DateParseFailureException;
import pn.nutrimeter.error.UserNotFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error/error";

    @ExceptionHandler(Throwable.class)
    @PageTitle("Error")
    public ModelAndView defaultExceptionHandler(Throwable e) throws Throwable {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        Throwable throwable = e;

        while (throwable.getCause() != null){
            throwable = throwable.getCause();
        }

        return this.getModelAndView(throwable);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @PageTitle("Error")
    public ModelAndView userNotFoundExceptionHandler(RuntimeException e) { return this.getModelAndView(e); }

    @ExceptionHandler(DateParseFailureException.class)
    @PageTitle("Error")
    public ModelAndView DateParseFailureExceptionHandler(DateTimeParseException e) { return this.getModelAndView(e); }

    private ModelAndView getModelAndView(Throwable e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("message", e.getMessage());
        return mav;
    }

}
