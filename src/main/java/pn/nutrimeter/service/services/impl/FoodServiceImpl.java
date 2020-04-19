package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.repositories.FoodCategoryRepository;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.validation.FoodValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodValidationService foodValidationService;

    private final FoodRepository foodRepository;

    private final FoodCategoryRepository foodCategoryRepository;

    private final ModelMapper modelMapper;

    public FoodServiceImpl(FoodValidationService foodValidationService, FoodRepository foodRepository, FoodCategoryRepository foodCategoryRepository1, ModelMapper modelMapper) {
        this.foodValidationService = foodValidationService;
        this.foodRepository = foodRepository;
        this.foodCategoryRepository = foodCategoryRepository1;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(FoodServiceModel foodServiceModel) {
        if (!this.foodValidationService.isValid(foodServiceModel)) {
            throw new FoodAddFailureException(ErrorConstants.INVALID_FOOD_MODEL);
        }

        foodServiceModel.setKcalPerHundredGrams(this.getTotalKcal(foodServiceModel));

        Food food = this.modelMapper.map(foodServiceModel, Food.class);

        food.setFoodCategories(foodServiceModel.getFoodCategories()
                .stream()
                .map(category -> this.foodCategoryRepository.findById(category.getId()).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_CATEGORY_ID)))
                .collect(Collectors.toList()));

        this.foodRepository.saveAndFlush(food);
    }

    @Override
    public List<FoodServiceModel> getAll() {
        return this.foodRepository.findAll()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public FoodServiceModel getById(String id) {
        Food food = this.foodRepository.findById(id).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_FOOD_ID));
        return this.modelMapper.map(food, FoodServiceModel.class);
    }

    private int getTotalKcal(FoodServiceModel food) {
        double proteins = food.getTotalProteins() == null
                ? 0.0
                : food.getTotalProteins();
        double carbs = food.getTotalCarbohydrates() == null
                ? 0.0
                : food.getTotalCarbohydrates();
        double lipids = food.getTotalLipids() == null
                ? 0.0
                : food.getTotalLipids();

        return (int) (proteins * 4 + carbs * 4 + lipids * 9);
    }
}
