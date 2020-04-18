package pn.nutrimeter.service.services.validation;

import org.springframework.stereotype.Service;
import pn.nutrimeter.service.models.FoodServiceModel;

@Service
public class FoodValidationServiceImpl implements FoodValidationService {

    @Override
    public boolean isValid(FoodServiceModel food) {
        return food != null
                && food.getName() != null && !food.getName().isEmpty()
                && food.getDescription() != null && !food.getDescription().isEmpty()
                && !food.getFoodCategories().isEmpty();
    }
}
