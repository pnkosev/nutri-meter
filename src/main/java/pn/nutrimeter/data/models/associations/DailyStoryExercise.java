package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Exercise;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_stories_exercises")
@IdClass(DailyStoryExerciseId.class)
public class DailyStoryExercise {

    @Column(name = "duration")
    private Double duration;

    @Id
    @ManyToOne
    @JoinColumn(name = "daily_story_id", referencedColumnName = "id")
    private DailyStory dailyStory;

    @Id
    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Exercise exercise;
}
