package pn.nutrimeter.annotations.validators;

import pn.nutrimeter.annotations.DateBeforeToday;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class DateBeforeTodayValidator implements ConstraintValidator<DateBeforeToday, LocalDate> {
   public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
      LocalDate today = LocalDate.now();
      return date != null && date.isBefore(today) && Period.between(date, today).getYears() <= 120;
   }
}
