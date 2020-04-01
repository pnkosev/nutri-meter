package pn.nutrimeter.service.services;

import pn.nutrimeter.service.models.FoodServiceModel;

import java.util.List;

public interface FoodService {

    void create(FoodServiceModel foodServiceModel);

    List<FoodServiceModel> getAll();
}
