package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.associations.DailyStoryFood;
import pn.nutrimeter.data.repositories.DailyStoryFoodRepository;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.service.services.api.DailyStoryFoodService;

import java.time.LocalDate;

@Service
public class DailyStoryFoodServiceImpl implements DailyStoryFoodService {

    private final DailyStoryFoodRepository dailyStoryFoodRepository;

    private final FoodRepository foodRepository;

    private final DailyStoryRepository dailyStoryRepository;

    private final ModelMapper modelMapper;

    public DailyStoryFoodServiceImpl(DailyStoryFoodRepository dailyStoryFoodRepository, FoodRepository foodRepository, DailyStoryRepository dailyStoryRepository, ModelMapper modelMapper) {
        this.dailyStoryFoodRepository = dailyStoryFoodRepository;
        this.foodRepository = foodRepository;
        this.dailyStoryRepository = dailyStoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(double quantity, LocalDate date, String foodId, String userId) {

        DailyStoryFood dailyStoryFood = new DailyStoryFood();

        DailyStory dailyStory = this.dailyStoryRepository.findByDateAndUserId(date, userId).orElseThrow(() -> new IllegalArgumentException("blabla"));
        Food food = this.foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("blabla"));

        dailyStoryFood.setGramsConsumed(quantity);
        dailyStoryFood.setFood(food);
        dailyStoryFood.setDailyStory(dailyStory);

        this.dailyStoryFoodRepository.save(dailyStoryFood);
    }
}
