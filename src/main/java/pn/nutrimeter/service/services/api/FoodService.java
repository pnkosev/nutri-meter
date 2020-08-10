package pn.nutrimeter.service.services.api;

import org.springframework.data.jpa.domain.Specification;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.util.List;

public interface FoodService {

    FoodServiceModel create(FoodServiceModel foodServiceModel);

    List<FoodServiceModel> getAll();

    List<FoodServiceModel> getAll(Specification<Food> specification);

    List<FoodServiceModel> getAllNonCustom();

    List<FoodServiceModel> getAllCustomOfUser();

    List<FoodServiceModel> getAllFavoritesOfUser();

    FoodServiceModel getById(String foodId);

    FoodServiceModel addFoodAsFavorite(String foodId);

    FoodServiceModel removeFoodAsFavorite(String foodId);
}
