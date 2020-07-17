package pn.nutrimeter.service.factories.daily_story;

import pn.nutrimeter.service.models.DailyStoryFoodServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.sql.Timestamp;

public interface DailyStoryFoodFactory {
    DailyStoryFoodServiceModel create(FoodServiceModel food, String measure, Double quantity, double gramsConsumedInPercentage, Timestamp timeOfDay);
}
