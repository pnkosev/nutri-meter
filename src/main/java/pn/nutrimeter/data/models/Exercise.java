package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.enums.ExerciseCategory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;

    @Column(name = "kcal_burned_per_min")
    private Double kcalBurnedPerMin;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "exercise")
    private List<DailyStoryExercise> dailyStoryExerciseAssociation;
}
