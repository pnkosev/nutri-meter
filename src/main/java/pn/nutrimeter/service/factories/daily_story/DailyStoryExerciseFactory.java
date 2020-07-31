package pn.nutrimeter.service.factories.daily_story;

import pn.nutrimeter.data.models.associations.DailyStoryExercise;
import pn.nutrimeter.service.models.DailyStoryExerciseServiceModel;
import pn.nutrimeter.service.models.ExerciseServiceModel;

public interface DailyStoryExerciseFactory {
    DailyStoryExerciseServiceModel create(DailyStoryExercise association, ExerciseServiceModel model);
}
