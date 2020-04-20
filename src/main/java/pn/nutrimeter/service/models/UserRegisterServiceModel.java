package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.enums.ActivityLevel;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.data.models.enums.Sex;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterServiceModel {

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    private Sex sex;

    private Double weight;

    private Double targetWeight;

    private Double height;

    private LocalDate birthday;

    private AgeCategory ageCategory;

    private ActivityLevel activityLevel;

    private Double bmr;

    private Double bmi;

    private Double bodyFat;

    private Double totalKcalTarget;

    private Set<RoleServiceModel> authorities;

//    private Double proteinTarget;
//
//    private Double cysteineRDA;
//
//    private Double histidineRDA;
//
//    private Double isoleucineRDA;
//
//    private Double leucineRDA;
//
//    private Double lysineRDA;
//
//    private Double methionineRDA;
//
//    private Double phenylalineRDA;
//
//    private Double threonineRDA;
//
//    private Double tryptophanRDA;
//
//    private Double tyrosineRDA;
//
//    private Double valineRDA;
//
//    private Double carbohydrateTarget;
//
//    private Double fiberRDA;
//
//    private Double lipidTarget;
//
//    private Double omega3RDA;
//
//    private Double omega6RDA;
//
//    private Double saturatedRDA;
//
//    private Double transFatsRDA;
}
