package pn.nutrimeter.service.factories.daily_story;

import pn.nutrimeter.service.models.DailyStoryNutrientServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.sql.Timestamp;

public interface DailyStoryFoodFactory {
    DailyStoryNutrientServiceModel create(FoodServiceModel food, double gramsConsumedInPercentage, Timestamp timeOfDay);
}
