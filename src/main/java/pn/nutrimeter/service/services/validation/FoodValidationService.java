package pn.nutrimeter.service.services.validation;

import pn.nutrimeter.service.models.FoodServiceModel;

public interface FoodValidationService {

    boolean isValid(FoodServiceModel food);
}
