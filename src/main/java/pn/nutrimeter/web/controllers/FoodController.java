package pn.nutrimeter.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pn.nutrimeter.annotation.PageTitle;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.FoodCategoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.models.TagServiceModel;
import pn.nutrimeter.service.services.api.FoodCategoryService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.api.MeasureService;
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
    public static final String FOOD_CATEGORY_ADD_EDIT_VIEW = "food/food-category-add-edit";
    public static final String FOOD_CATEGORY_EDIT_URL = "/category/edit/{categoryId}";
    public static final String FOOD_TAG_ADD_URL = "/tag/add";
    public static final String FOOD_TAG_EDIT_URL = "/tag/edit/{tagId}";
    public static final String FOOD_TAG_ADD_EDIT_VIEW = "food/tag-add-edit";
    public static final String REDIRECT_HOME_URL = "/home";
    public static final String REDIRECT_FOOD_CATEGORY_URL = "/user/admin-tool#categories";
    public static final String REDIRECT_TAG_URL = "/user/admin-tool#tags";

    private final FoodService foodService;

    private final FoodCategoryService foodCategoryService;

    private final TagService tagService;

    private final MeasureService measureService;

    private final ModelMapper modelMapper;

    public FoodController(FoodService foodService, FoodCategoryService foodCategoryService, TagService tagService, MeasureService measureService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.foodCategoryService = foodCategoryService;
        this.tagService = tagService;
        this.measureService = measureService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(FOOD_ADD_URL)
    @PageTitle("Add Food")
    public ModelAndView addFood(FoodCreateBindingModel foodCreateBindingModel) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("foodCategories", this.foodCategoryService.getAll());
        mav.addObject("tags", this.tagService.getAll());
        return view(mav, FOOD_ADD_VIEW);
    }

    @PostMapping(FOOD_ADD_URL)
    public ModelAndView addFoodPost(
            @Valid FoodCreateBindingModel foodCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("foodCategories", this.foodCategoryService.getAll());
            mav.addObject("tags", this.tagService.getAll());
            mav.addObject("measures", this.measureService.getAllFromList(foodCreateBindingModel.getMeasures()));
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

        List<String> tags = foodCreateBindingModel.getTags();

        foodServiceModel.setTags(
                tags
                .stream()
                .map(id -> {
                    TagServiceModel tagServiceModel = new TagServiceModel();
                    tagServiceModel.setId(id);
                    return tagServiceModel;
                })
                .collect(Collectors.toList()));

        List<String> measures = foodCreateBindingModel.getMeasures();

        foodServiceModel.setMeasures(
                measures
                        .stream()
                        .map(id -> {
                            MeasureServiceModel measureServiceModel = new MeasureServiceModel();
                            measureServiceModel.setId(id);
                            return measureServiceModel;
                        })
                        .collect(Collectors.toList()));

        try {
            this.foodService.create(foodServiceModel);
        } catch (FoodAddFailureException | IdNotFoundException e) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("foodCategories", this.foodCategoryService.getAll());
            mav.addObject("tags", this.tagService.getAll());
            mav.addObject("msg", e.getMessage());
            return view(mav, FOOD_ADD_VIEW, e.getHttpStatus());
        }

        return redirect(REDIRECT_HOME_URL);
    }

    @GetMapping(FOOD_CATEGORY_ADD_URL)
    @PageTitle("Add Category")
    public ModelAndView addCategory(FoodCategoryCreateBindingModel foodCategoryCreateBindingModel) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("action", "Add");
        return view(mav, FOOD_CATEGORY_ADD_EDIT_VIEW);
    }

    @PostMapping(FOOD_CATEGORY_ADD_URL)
    public ModelAndView addCategoryPost(
            @Valid FoodCategoryCreateBindingModel foodCategoryCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(FOOD_CATEGORY_ADD_EDIT_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.foodCategoryService.create(this.modelMapper.map(foodCategoryCreateBindingModel, FoodCategoryServiceModel.class));

        return redirect(REDIRECT_FOOD_CATEGORY_URL);
    }

    @GetMapping(FOOD_CATEGORY_EDIT_URL)
    @PageTitle("Edit Category")
    public ModelAndView editCategory(
            @PathVariable String categoryId,
            FoodCategoryCreateBindingModel foodCategoryCreateBindingModel) {
        FoodCategoryServiceModel foodCategoryServiceModel = this.foodCategoryService.getById(categoryId);
        foodCategoryCreateBindingModel.setName(foodCategoryServiceModel.getName());
        foodCategoryCreateBindingModel.setDescription(foodCategoryServiceModel.getDescription());
        ModelAndView mav = new ModelAndView();
        mav.addObject("action", "Edit");
        mav.addObject("categoryId", foodCategoryServiceModel.getId());

        return view(mav, FOOD_CATEGORY_ADD_EDIT_VIEW);
    }

    @PostMapping(FOOD_CATEGORY_EDIT_URL)
    public ModelAndView editCategoryPost(
            @PathVariable String categoryId,
            @Valid FoodCategoryCreateBindingModel foodCategoryCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(FOOD_CATEGORY_ADD_EDIT_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        FoodCategoryServiceModel foodCategoryServiceModel = this.modelMapper.map(foodCategoryCreateBindingModel, FoodCategoryServiceModel.class);
        foodCategoryServiceModel.setId(categoryId);
        this.foodCategoryService.edit(foodCategoryServiceModel);

        return redirect(REDIRECT_FOOD_CATEGORY_URL);
    }

    @GetMapping(FOOD_TAG_ADD_URL)
    @PageTitle("Add Tag")
    public ModelAndView addTag(TagCreateBindingModel tagCreateBindingModel) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("action", "Add");
        return view(mav, FOOD_TAG_ADD_EDIT_VIEW);
    }

    @PostMapping(FOOD_TAG_ADD_URL)
    public ModelAndView addTagPost(
            @Valid TagCreateBindingModel tagCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(FOOD_TAG_ADD_EDIT_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.tagService.create(this.modelMapper.map(tagCreateBindingModel, TagServiceModel.class));

        return redirect(REDIRECT_TAG_URL);
    }

    @GetMapping(FOOD_TAG_EDIT_URL)
    @PageTitle("Edit Tag")
    public ModelAndView editTag(
            @PathVariable String tagId,
            TagCreateBindingModel tagCreateBindingModel) {
        TagServiceModel tagServiceModel = this.tagService.getById(tagId);
        tagCreateBindingModel.setName(tagServiceModel.getName());
        tagCreateBindingModel.setDescription(tagServiceModel.getDescription());
        ModelAndView mav = new ModelAndView();
        mav.addObject("action", "Edit");
        mav.addObject("tagId", tagServiceModel.getId());

        return view(mav, FOOD_TAG_ADD_EDIT_VIEW);
    }

    @PostMapping(FOOD_TAG_EDIT_URL)
    public ModelAndView editTagPost(
            @PathVariable String tagId,
            @Valid TagCreateBindingModel tagCreateBindingModel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view(FOOD_TAG_ADD_EDIT_VIEW, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        TagServiceModel tagServiceModel = this.modelMapper.map(tagCreateBindingModel, TagServiceModel.class);
        tagServiceModel.setId(tagId);
        this.tagService.edit(tagServiceModel);

        return redirect(REDIRECT_TAG_URL);
    }
}
