package pn.nutrimeter.annotations.validators;

import pn.nutrimeter.annotations.UserPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {

   public boolean isValid(String password, ConstraintValidatorContext context) {
      return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$");
   }
}
