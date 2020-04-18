package pn.nutrimeter.annotations;

import pn.nutrimeter.annotations.validators.UserPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserPasswordValidator.class)
@Documented
public @interface UserPassword {

    String message() default "Password must have at least 1 lower, 1 upper case letters and 1 digit and must be between 8 and 20 characters!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
