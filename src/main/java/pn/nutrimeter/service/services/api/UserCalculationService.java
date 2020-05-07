package pn.nutrimeter.service.services.api;

import pn.nutrimeter.data.models.enums.AgeCategory;

import java.time.LocalDate;

public interface UserCalculationService {

    double getYearsOld(LocalDate birthday);

    AgeCategory updateAgeCategory(double yearsOld);

    double calculateBMR(String sex, double weight, double height, double yearsOld);

    double calculateBMI(double weight, double height);

    double calculateBodyFat(double yearsOld, String ageCategory, String sex, double bmi);

    double calculateKcalFromActivityLevel(String activityLevel, double bmr);

    double calculateKcalFromWeightTarget(double kilosPerWeek);

    double calculateTotalKcal(double bmr, double kcalFromActivityLevel, double targetWeight, double weight, double kcalFromTarget);

    double calculateKcalFromTotal(double totalKcalTarget, double percentage);
}
