package pn.nutrimeter.service.services.impl;

import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.repositories.DailyStoryFoodRepository;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.error.DailyStoryNotFoundException;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryFoodService;

import java.time.LocalDate;

@Service
public class DailyStoryFoodServiceImpl implements DailyStoryFoodService {

    private final DailyStoryFoodRepository dailyStoryFoodRepository;

    private final FoodRepository foodRepository;

    private final DailyStoryRepository dailyStoryRepository;

    public DailyStoryFoodServiceImpl(DailyStoryFoodRepository dailyStoryFoodRepository, FoodRepository foodRepository, DailyStoryRepository dailyStoryRepository) {
        this.dailyStoryFoodRepository = dailyStoryFoodRepository;
        this.foodRepository = foodRepository;
        this.dailyStoryRepository = dailyStoryRepository;
    }

    @Override
    public void create(String measure, Double equivalentInGrams, double quantity, LocalDate date, String foodId, String userId) {

        DailyStoryFood dailyStoryFood = new DailyStoryFood();

        DailyStory dailyStory = this.dailyStoryRepository.findByDateAndUserId(date, userId).orElseThrow(() -> new DailyStoryNotFoundException(ErrorConstants.DAILY_STORY_NOT_FOUND));
        Food food = this.foodRepository.findById(foodId).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_FOOD_ID));

        dailyStoryFood.setMeasure(measure);
        dailyStoryFood.setQuantity(quantity);
        dailyStoryFood.setGramsConsumed(equivalentInGrams * quantity);
        dailyStoryFood.setFood(food);
        dailyStoryFood.setDailyStory(dailyStory);

        this.dailyStoryFoodRepository.save(dailyStoryFood);
    }

    @Override
    public void delete(String dailyStoryFoodId) {
        DailyStoryFood dailyStoryFood = this.dailyStoryFoodRepository
                .findById(dailyStoryFoodId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.DAILY_STORY_FOOD_NOT_FOUND));
        this.dailyStoryFoodRepository.delete(dailyStoryFood);
    }
}
