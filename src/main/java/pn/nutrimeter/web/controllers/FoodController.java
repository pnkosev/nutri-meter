package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.web.models.binding.FoodCategoryCreateBindingModel;
import pn.nutrimeter.web.models.binding.FoodCreateBindingModel;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/food")
public class FoodController extends BaseController {

    private final FoodService foodService;

    private final FoodCategoryService foodCategoryService;

    private final ModelMapper modelMapper;

    public FoodController(FoodService foodService, FoodCategoryService foodCategoryService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.foodCategoryService = foodCategoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView addFood(FoodCreateBindingModel foodCreateBindingModel) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("foodCategories", this.foodCategoryService.getAll());
        return view(mav, "food/food-add");
    }

    @PostMapping("/add")
    public ModelAndView addFoodPost(
            @Valid FoodCreateBindingModel foodCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("foodCategories", this.foodCategoryService.getAll());
            return view(mav, "food/food-add", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        FoodServiceModel foodServiceModel = this.modelMapper.map(foodCreateBindingModel, FoodServiceModel.class);

        List<String> foodCategories = foodCreateBindingModel.getFoodCategories();

        foodServiceModel.setFoodCategories(
                foodCategories
                .stream()
                .map(id -> {
                    FoodCategoryServiceModel foodCategoryServiceModel = new FoodCategoryServiceModel();
                    foodCategoryServiceModel.setId(id);
                    return foodCategoryServiceModel;
                })
                .collect(Collectors.toList()));

        try {
            this.foodService.create(foodServiceModel);
        } catch (FoodAddFailureException | IdNotFoundException e) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("foodCategories", this.foodCategoryService.getAll());
            mav.addObject("msg", e.getMessage());
            return view(mav, "food/food-add", e.getHttpStatus());
        }

        return redirect("/home");
    }

    @GetMapping("/category/add")
    public ModelAndView addCategory(FoodCategoryCreateBindingModel foodCategoryCreateBindingModel) {
        return view("food/food-category-add");
    }

    @PostMapping("/category/add")
    public ModelAndView addCategoryPost(
            @Valid FoodCategoryCreateBindingModel foodCategoryCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view("food/food-category-add", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.foodCategoryService.create(this.modelMapper.map(foodCategoryCreateBindingModel, FoodCategoryServiceModel.class));

        return redirect("/home");
    }
}
