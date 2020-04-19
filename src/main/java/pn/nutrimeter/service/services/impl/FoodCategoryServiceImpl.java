package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.repositories.FoodCategoryRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodCategoryServiceImpl implements FoodCategoryService {

    private final FoodCategoryRepository foodCategoryRepository;

    private final ModelMapper modelMapper;

    public FoodCategoryServiceImpl(FoodCategoryRepository foodCategoryRepository, ModelMapper modelMapper) {
        this.foodCategoryRepository = foodCategoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(FoodCategoryServiceModel foodCategoryServiceModel) {
        this.foodCategoryRepository.saveAndFlush(this.modelMapper.map(foodCategoryServiceModel, FoodCategory.class));
    }

    @Override
    public FoodCategoryServiceModel getById(String id) {
        return this.modelMapper.map(this.foodCategoryRepository.findById(id).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_CATEGORY_ID)), FoodCategoryServiceModel.class);
    }

    @Override
    public List<FoodCategoryServiceModel> getAll() {
        return this.foodCategoryRepository.findAll()
                .stream()
                .map(fc -> this.modelMapper.map(fc, FoodCategoryServiceModel.class))
                .collect(Collectors.toList());
    }
}
