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

    @Column(name = "bmr")
    private Double bmr;

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "body_fat")
    private Double bodyFat;

    @Column(name = "protein_target")
    private Double proteinTarget;

    @Column(name = "cystein_rda")
    private Double cysteinRDA;

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

    @Column(name = "carbohydrate_target")
    private Double carbohydrateTarget;

    @Column(name = "fiber_rda")
    private Double fiberRDA;

    @Column(name = "lipid_target")
    private Double lipidTarget;

    @Column(name = "omega3_rda")
    private Double omega3RDA;

    @Column(name = "omega6_rda")
    private Double omega6RDA;

    @Column(name = "saturated_rda")
    private Double saturatedRDA;

    @Column(name = "trans_fats_rda")
    private Double transFatsRDA;

    @ManyToOne
    @JoinColumn(name = "target_id", referencedColumnName = "id")
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
}
