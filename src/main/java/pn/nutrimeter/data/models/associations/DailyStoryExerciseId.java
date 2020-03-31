package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Exercise;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class DailyStoryExerciseId implements Serializable {

    private DailyStory dailyStory;

    private Exercise exercise;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyStoryExerciseId that = (DailyStoryExerciseId) o;
        return Objects.equals(dailyStory, that.dailyStory) &&
                Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyStory, exercise);
    }
}
