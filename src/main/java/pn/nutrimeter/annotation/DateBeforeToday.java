package pn.nutrimeter.annotation;

import pn.nutrimeter.annotation.validators.DateBeforeTodayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DateBeforeTodayValidator.class)
public @interface DateBeforeToday {

    String message() default "Please enter a valid date!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

