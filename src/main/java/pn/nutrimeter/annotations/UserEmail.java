package pn.nutrimeter.annotations;

import pn.nutrimeter.annotations.validators.UserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserEmailValidator.class)
@Documented
public @interface UserEmail {

    String message() default "Incorrect email format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
