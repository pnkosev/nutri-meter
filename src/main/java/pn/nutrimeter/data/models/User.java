package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.enums.ActivityLevel;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "target_weight")
    private Double targetWeight;

    @Column(name = "kilos_per_week")
    private Double kilosPerWeek;

    @Column(name = "kcal_from_target")
    private Double kcalFromTarget;

    @Column(name = "height")
    private Double height;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "age_category")
    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;

    @Column(name = "activity_level")
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(name = "kcal_from_bmr")
    private Double bmr;

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "body_fat")
    private Double bodyFat;

    @Column(name = "kcal_from_activity_level")
    private Double kcalFromActivityLevel;

    @Column(name = "total_kcal_target")
    private Double totalKcalTarget;

    @Column(name = "proteins_target_in_percentage")
    private Double proteinTargetInPercentage;

    @Column(name = "protein_target_in_kcal")
    private Double proteinTargetInKcal;

    @Column(name = "cysteine_rda")
    private Double cysteineRDA;

    @Column(name = "histidine_rda")
    private Double histidineRDA;

    @Column(name = "isoleucine_rda")
    private Double isoleucineRDA;

    @Column(name = "leucine_rda")
    private Double leucineRDA;

    @Column(name = "lysine_rda")
    private Double lysineRDA;

    @Column(name = "methionine_rda")
    private Double methionineRDA;

    @Column(name = "phenylaline_rda")
    private Double phenylalineRDA;

    @Column(name = "threonine_rda")
    private Double threonineRDA;

    @Column(name = "tryptophan_rda")
    private Double tryptophanRDA;

    @Column(name = "tyrosine_rda")
    private Double tyrosineRDA;

    @Column(name = "valine_rda")
    private Double valineRDA;

    @Column(name = "carbs_target_in_percentage")
    private Double carbohydrateTargetInPercentage;

    @Column(name = "carbohydrate_target_in_kcal")
    private Double carbohydrateTargetInKcal;  // concerns the target as %

    @Column(name = "lipid_target_in_percentage")
    private Double lipidTargetInPercentage;

    @Column(name = "lipid_target_in_kcal")
    private Double lipidTargetInKcal;  // concerns the target as %

    @ManyToOne
    @JoinColumn(name = "life_group_stage_id", referencedColumnName = "id")
    private LifeStageGroup lifeStageGroup;

    @ManyToOne
    @JoinColumn(name = "macro_target_id", referencedColumnName = "id")
    private MacroTarget macroTarget;

    @ManyToOne
    @JoinColumn(name = "micro_target_id", referencedColumnName = "id")
    private MicroTarget microTarget;

    @OneToMany(mappedBy = "user")
    private List<Food> customFoods;

    @OneToMany(mappedBy = "user")
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "user")
    private List<DailyStory> dailyStories;

    @ManyToMany
    @JoinTable(
            name = "users_foods",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id")
    )
    private List<Food> favoriteFoods;

    private String getGoalAsString() {
        if (this.targetWeight - this.weight > 0) {
            return "gain";
        } else if (this.targetWeight - this.weight < 0) {
            return "lose";
        } else {
            return "maintain";
        }
    }

    private int getYearsOld() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }

    public AgeCategory updateAgeCategory() {
        int yearsOld = this.getYearsOld();

        if (yearsOld < 4) {
            return AgeCategory.INFANT;
        } else if (yearsOld < 9) {
            return AgeCategory.CHILDREN;
        } else if (yearsOld < 21) {
            return AgeCategory.ADOLESCENT;
        } else if (yearsOld < 59) {
            return AgeCategory.ADULT;
        } else {
            return AgeCategory.ELDERLY;
        }
    }

    public Double calculateBMR() {
        return sex.name().equalsIgnoreCase("male")
                ? (10 * this.weight) + (6.25 * this.height) - (5 * getYearsOld()) + 5
                : (10 * this.weight) + (6.25 * this.height) - (5 * getYearsOld()) - 161;
    }

    public Double calculateBMI() {
        return this.weight / Math.pow((this.height / 100), 2);
    }

    public Double calculateBodyFat() {
        int yearsOld = this.getYearsOld();
        switch (this.ageCategory.name().toLowerCase()) {
            case "adolescent":
                return sex.name().equalsIgnoreCase("male")
                        ? 1.51 * bmi - 0.70 * yearsOld - 2.2
                        : 1.51 * bmi - 0.70 * yearsOld + 1.4;
            case "adult":
                return sex.name().equalsIgnoreCase("male")
                        ? 1.20 * bmi + 0.23 * yearsOld - 16.2
                        : 1.20 * bmi + 0.23 * yearsOld - 5.4;
            default: return null;
        }
    }

    public Double calculateKcalFromActivityLevel() {
        Double variable = 0.0;
        switch (this.activityLevel.name().toLowerCase()) {
            case "sedentary": variable = 1.2; break;
            case "light": variable = 1.375; break;
            case "moderate": variable = 1.55; break;
            case "very": variable = 1.725; break;
        }
        return this.bmr * variable - this.bmr;
    }

    public Double calculateKcalFromTotal(Double percentage) {
        return this.totalKcalTarget * percentage;
    }

    public Double calculateKcalFromWeightTarget() {
        return 1000 * this.kilosPerWeek;
    }

    public Double calculateTotalKcal() {
        Double total = this.bmr + this.kcalFromActivityLevel;

        switch (this.getGoalAsString()) {
            case "gain": total += this.kcalFromTarget; break;
            case "lose": total -= this.kcalFromTarget; break;
            default: break;
        }

        return total;
    }
}
