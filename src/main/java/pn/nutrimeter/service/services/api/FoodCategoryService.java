package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.FoodCategoryServiceModel;

import java.util.List;

public interface FoodCategoryService {

    void create(FoodCategoryServiceModel foodCategoryServiceModel);

    FoodCategoryServiceModel getById(String id);

    List<FoodCategoryServiceModel> getAll();

    void deleteCategory(String categoryId);
}
