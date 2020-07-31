package pn.nutrimeter.service.factories.daily_story;

import pn.nutrimeter.annotation.Factory;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;
import pn.nutrimeter.service.models.DailyStoryExerciseServiceModel;
import pn.nutrimeter.service.models.ExerciseServiceModel;

@Factory
public class DailyStoryExerciseFactoryImpl implements DailyStoryExerciseFactory {

    @Override
    public DailyStoryExerciseServiceModel create(DailyStoryExercise association, ExerciseServiceModel model) {
        DailyStoryExerciseServiceModel dailyStoryExerciseServiceModel = new DailyStoryExerciseServiceModel();

        dailyStoryExerciseServiceModel.setId(association.getId());
        dailyStoryExerciseServiceModel.setName(model.getName());
        dailyStoryExerciseServiceModel.setKcalBurnedPerMin(model.getKcalBurnedPerMin());
        dailyStoryExerciseServiceModel.setDuration(association.getDuration());

        return dailyStoryExerciseServiceModel;
    }
}
