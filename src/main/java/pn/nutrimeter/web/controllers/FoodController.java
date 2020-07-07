package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.models.TagServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.api.TagService;
import pn.nutrimeter.web.models.binding.FoodCategoryCreateBindingModel;
import pn.nutrimeter.web.models.binding.FoodCreateBindingModel;
import pn.nutrimeter.web.models.binding.TagCreateBindingModel;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/food")
public class FoodController extends BaseController {

    public static final String FOOD_ADD_URL = "/add";
    public static final String FOOD_ADD_VIEW = "food/food-add";
    public static final String FOOD_CATEGORY_ADD_URL = "/category/add";
    public static final String FOOD_CATEGORY_ADD_VIEW = "food/food-category-add";
    public static final String REDIRECT_URL = "/home";
    public static final String FOOD_TAG_ADD_URL = "/tag/add";
    public static final String FOOD_TAG_ADD_VIEW = "food/tag-add";

    private final FoodService foodService;

    private final FoodCategoryService foodCategoryService;

    private final TagService tagService;

    private final ModelMapper modelMapper;

    public FoodController(FoodService foodService, FoodCategoryService foodCategoryService, TagService tagService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.foodCategoryService = foodCategoryService;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(FOOD_ADD_URL)
    @PageTitle("Add Food")
    public ModelAndView addFood(FoodCreateBindingModel foodCreateBindingModel) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("foodCategories", this.foodCategoryService.getAll());
        return view(mav, FOOD_ADD_VIEW);
    }

    @PostMapping(FOOD_ADD_URL)
    public ModelAndView addFoodPost(
            @Valid FoodCreateBindingModel foodCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("foodCategories", this.foodCategoryService.getAll());
            return view(mav, FOOD_ADD_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
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
            return view(mav, FOOD_ADD_VIEW, e.getHttpStatus());
        }

        return redirect(REDIRECT_URL);
    }

    @GetMapping(FOOD_CATEGORY_ADD_URL)
    @PageTitle("Add Category")
    public ModelAndView addCategory(FoodCategoryCreateBindingModel foodCategoryCreateBindingModel) {
        return view(FOOD_CATEGORY_ADD_VIEW);
    }

    @PostMapping(FOOD_CATEGORY_ADD_URL)
    public ModelAndView addCategoryPost(
            @Valid FoodCategoryCreateBindingModel foodCategoryCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(FOOD_CATEGORY_ADD_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.foodCategoryService.create(this.modelMapper.map(foodCategoryCreateBindingModel, FoodCategoryServiceModel.class));

        return redirect(REDIRECT_URL);
    }

    @GetMapping(FOOD_TAG_ADD_URL)
    @PageTitle("Add Tag")
    public ModelAndView addTag(TagCreateBindingModel tagCreateBindingModel) {
        return view(FOOD_TAG_ADD_VIEW);
    }

    @PostMapping(FOOD_TAG_ADD_URL)
    public ModelAndView addTagPost(
            @Valid TagCreateBindingModel tagCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(FOOD_TAG_ADD_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.tagService.create(this.modelMapper.map(tagCreateBindingModel, TagServiceModel.class));

        return redirect(REDIRECT_URL);
    }
}
