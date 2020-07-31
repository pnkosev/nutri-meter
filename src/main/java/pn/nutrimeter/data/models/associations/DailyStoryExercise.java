package pn.nutrimeter.data.models.associations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Exercise;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daily_stories_exercises")
//@IdClass(DailyStoryExerciseId.class)
public class DailyStoryExercise extends BaseEntity {

    @Column(name = "duration")
    private Double duration;

    @ManyToOne
    @JoinColumn(name = "daily_story_id", referencedColumnName = "id")
    private DailyStory dailyStory;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Exercise exercise;
}
