package pn.nutrimeter.annotation.validators;

import pn.nutrimeter.annotation.UserEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<UserEmail, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email.matches("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,6}$");
    }
}
