package pn.nutrimeter.service.services.impl;

import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.service.services.api.UserCalculationService;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserCalculationServiceImpl implements UserCalculationService {

    @Override
    public double getYearsOld(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    @Override
    public AgeCategory updateAgeCategory(double yearsOld) {
        if (yearsOld < 4) {
            return AgeCategory.INFANT;
        } else if (yearsOld < 9) {
            return AgeCategory.CHILD;
        } else if (yearsOld < 21) {
            return AgeCategory.ADOLESCENT;
        } else if (yearsOld < 59) {
            return AgeCategory.ADULT;
        } else {
            return AgeCategory.ELDERLY;
        }
    }

    @Override
    public double calculateBMR(String sex, double weight, double height, double yearsOld) {
        return sex.equalsIgnoreCase("male")
                ? (10 * weight) + (6.25 * height) - (5 * yearsOld) + 5
                : (10 * weight) + (6.25 * height) - (5 * yearsOld) - 161;
    }

    @Override
    public double calculateBMI(double weight, double height) {
        return weight / Math.pow((height / 100), 2);
    }

    @Override
    public double calculateBodyFat(double yearsOld, String ageCategory, String sex, double bmi) {
        switch (ageCategory.toLowerCase()) {
            case "adolescent":
                return sex.equalsIgnoreCase("male")
                        ? 1.51 * bmi - 0.70 * yearsOld - 2.2
                        : 1.51 * bmi - 0.70 * yearsOld + 1.4;
            case "adult":
                return sex.equalsIgnoreCase("male")
                        ? 1.20 * bmi + 0.23 * yearsOld - 16.2
                        : 1.20 * bmi + 0.23 * yearsOld - 5.4;
            default: return 0.0;
        }
    }

    @Override
    public double calculateKcalFromActivityLevel(String activityLevel, double bmr) {
        double variable = 0.0;

        switch (activityLevel.toLowerCase()) {
            case "sedentary": variable = 1.2; break;
            case "light": variable = 1.375; break;
            case "moderate": variable = 1.55; break;
            case "very": variable = 1.725; break;
        }
        return bmr * variable - bmr;
    }

    @Override
    public double calculateKcalFromWeightTarget(double kilosPerWeek) {
        return 1000 * kilosPerWeek;
    }

    @Override
    public double calculateTotalKcal(double bmr, double kcalFromActivityLevel, double targetWeight, double weight, double kcalFromTarget) {
        double total = bmr + kcalFromActivityLevel;

        switch (this.getGoalAsString(targetWeight, weight)) {
            case "gain": total += kcalFromTarget; break;
            case "lose": total -= kcalFromTarget; break;
            default: break;
        }

        return total;
    }

    @Override
    public double calculateKcalFromTotal(double totalKcalTarget, double percentage) {
        return totalKcalTarget * percentage;
    }

    private String getGoalAsString(double targetWeight, double weight) {
        if (targetWeight - weight > 0) {
            return "gain";
        } else if (targetWeight - weight < 0) {
            return "lose";
        } else {
            return "maintain";
        }
    }
}
