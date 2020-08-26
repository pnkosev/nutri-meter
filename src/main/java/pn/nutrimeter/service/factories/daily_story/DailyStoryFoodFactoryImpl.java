package pn.nutrimeter.service.factories.daily_story;

import org.springframework.stereotype.Service;
import pn.nutrimeter.annotation.Factory;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.service.models.DailyStoryFoodServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.sql.Timestamp;

@Factory
public class DailyStoryFoodFactoryImpl implements DailyStoryFoodFactory {
    @Override
    public DailyStoryFoodServiceModel create(DailyStoryFood association, FoodServiceModel food) {
        DailyStoryFoodServiceModel dailyStoryFoodServiceModel = new DailyStoryFoodServiceModel();

        double gramsConsumedInPercentage = association.getGramsConsumed() / 100;

        dailyStoryFoodServiceModel.setId(association.getId());
        dailyStoryFoodServiceModel.setTimeOfDay(association.getTimeOfDay());
        dailyStoryFoodServiceModel.setName(food.getName());
        dailyStoryFoodServiceModel.setMeasure(association.getMeasure());
        dailyStoryFoodServiceModel.setQuantity(association.getQuantity());
        dailyStoryFoodServiceModel.setGramsConsumed(gramsConsumedInPercentage * 100);
        dailyStoryFoodServiceModel.setKcal((food.getKcalPerHundredGrams() * gramsConsumedInPercentage));

        return dailyStoryFoodServiceModel;
    }
}
