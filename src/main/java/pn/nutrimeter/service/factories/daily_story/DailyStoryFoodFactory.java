package pn.nutrimeter.service.factories.daily_story;

import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.service.models.DailyStoryFoodServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.sql.Timestamp;

public interface DailyStoryFoodFactory {
    DailyStoryFoodServiceModel create(DailyStoryFood association, FoodServiceModel food);
}
