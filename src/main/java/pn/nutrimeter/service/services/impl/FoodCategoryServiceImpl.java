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
        FoodCategory foodCategory = this.modelMapper.map(foodCategoryServiceModel, FoodCategory.class);
        this.foodCategoryRepository.saveAndFlush(foodCategory);
    }

    @Override
    public FoodCategoryServiceModel getById(String id) {
        FoodCategory foodCategory = this.foodCategoryRepository.findById(id).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_CATEGORY_ID));
        return this.modelMapper.map(foodCategory, FoodCategoryServiceModel.class);
    }

    @Override
    public List<FoodCategoryServiceModel> getAll() {
        return this.foodCategoryRepository.findAll()
                .stream()
                .map(fc -> this.modelMapper.map(fc, FoodCategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(String categoryId) {
        FoodCategory foodCategory = this.foodCategoryRepository.findById(categoryId).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_CATEGORY_ID));
        this.foodCategoryRepository.delete(foodCategory);
    }

    @Override
    public void edit(FoodCategoryServiceModel foodCategoryServiceModel) {
        FoodCategory foodCategory = this.foodCategoryRepository.findById(foodCategoryServiceModel.getId()).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_CATEGORY_ID));
        this.modelMapper.map(foodCategoryServiceModel, foodCategory);
        this.foodCategoryRepository.saveAndFlush(foodCategory);
    }
}
