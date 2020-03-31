package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_stories")
public class DailyStory extends BaseEntity {

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "dailyStory")
    private List<DailyStoryFood> dailyStoryFoodAssociation;

    @OneToMany(mappedBy = "exercise")
    private List<DailyStoryExercise> dailyStoryExerciseAssociation;
}
