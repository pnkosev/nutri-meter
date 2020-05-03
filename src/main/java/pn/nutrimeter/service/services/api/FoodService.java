package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.FoodServiceModel;

import java.util.List;

public interface FoodService {

    FoodServiceModel create(FoodServiceModel foodServiceModel);

    List<FoodServiceModel> getAll();

    List<FoodServiceModel> getAllNonCustom();

    List<FoodServiceModel> getAllCustomOfUser();

    FoodServiceModel getById(String id);
}
