package pn.nutrimeter.web.controllers;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ModelAndView defaultExceptionHandler(Throwable e) throws Throwable {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return this.getModelAndView(e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView userNotFoundExceptionHandler(RuntimeException e) {
        return this.getModelAndView(e);
    }

    private ModelAndView getModelAndView(Throwable e) {
        ModelAndView mav = new ModelAndView(ErrorConstants.DEFAULT_ERROR_VIEW);
        mav.addObject("message", e.getMessage());
        return mav;
    }
}
