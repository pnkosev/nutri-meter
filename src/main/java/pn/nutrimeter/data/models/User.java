package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.enums.ActivityLevel;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@NamedStoredProcedureQuery(
        name = "usp_update_user",
        procedureName = "usp_update_user",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "user_id", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "life_stage_group_id", type = String.class)
        })
public class User extends BaseEntity implements UserDetails {

    @Column(name = "username", nullable = false, unique = true, length = 15)
    private String username;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "weight", nullable = false, precision = 5, scale = 2)
    private Double weight;

    @Column(name = "target_weight")
    private Double targetWeight;

    @Column(name = "kilos_per_week")
    private Double kilosPerWeek;

    @Column(name = "kcal_from_target")
    private Double kcalFromTarget;

    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    private Double height;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "years_old", nullable = false, precision = 3, scale = 1)
    private Double yearsOld;

    @Column(name = "age_category")
    @Enumerated(EnumType.STRING)
    private AgeCategory ageCategory;

    @Column(name = "activity_level", nullable = false)
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
    private List<Exercise> customExercises;

    @OneToMany(mappedBy = "user")
    private List<DailyStory> dailyStories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> authorities;

    @ManyToMany
    @JoinTable(
            name = "users_foods",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id")
    )
    private List<Food> favoriteFoods;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
