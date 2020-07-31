package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.models.specifications.*;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilderImpl;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilder;
import pn.nutrimeter.service.facades.AuthenticationFacade;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryFoodService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.api.MeasureService;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.binding.DailyFoodBindingModel;
import pn.nutrimeter.web.models.view.FoodDetailedViewModel;
import pn.nutrimeter.web.models.view.FoodSimpleViewModel;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FoodRestController {

    private final FoodService foodService;

    private final UserService userService;

    private final MeasureService measureService;

    private final DailyStoryFoodService dailyStoryFoodService;

    private final AuthenticationFacade authenticationFacade;

    private final ModelMapper modelMapper;

    public FoodRestController(FoodService foodService, UserService userService, MeasureService measureService, DailyStoryFoodService dailyStoryFoodService, AuthenticationFacade authenticationFacade, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.userService = userService;
        this.measureService = measureService;
        this.dailyStoryFoodService = dailyStoryFoodService;
        this.authenticationFacade = authenticationFacade;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/foods-all")
    public List<FoodSimpleViewModel> allFoods() {
        return this.foodService.getAllNonCustom()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/foods-custom")
    public List<FoodSimpleViewModel> allCustomFoods() {
        return this.foodService.getAllCustomOfUser()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/foods-favorite")
    public List<FoodSimpleViewModel> allFavoriteFoods() {
        return this.foodService.getAllFavoritesOfUser()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/foods-favorite")
    public void toggleFavoriteFood(@RequestBody Map<String, String> payload) {
        String foodId = payload.get("foodId");
        boolean isFavorite = payload.get("isFavorite").equalsIgnoreCase("true");

        if (isFavorite) {
            this.foodService.addFoodAsFavorite(foodId);
        } else {
            this.foodService.removeFoodAsFavorite(foodId);
        }
    }

    @GetMapping("/foods")
    public List<FoodSimpleViewModel> searchedFoods(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "category") String category
    ) {
        Specification<Food> specification = getFoodSpecification(name, type, category);

        return this.foodService.getAll(specification)
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/food/{foodId}")
    public FoodDetailedViewModel getFood(@PathVariable String foodId) {
        return this.modelMapper.map(this.foodService.getById(foodId), FoodDetailedViewModel.class);
    }

    @PostMapping("/food/{foodId}")
    public void addFood(
            @PathVariable String foodId,
            @RequestBody DailyFoodBindingModel model,
            Principal principal) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(model.getDate(), formatter);
        MeasureServiceModel measure = this.measureService.getById(model.getMeasure());
        double quantity = Double.parseDouble(model.getQuantity());
        UserServiceModel userModel = this.userService.getUserByUsername(principal.getName());

        this.dailyStoryFoodService.create(measure.getName(), measure.getEquivalentInGrams(), quantity, date, foodId, userModel.getId());
    }

    @DeleteMapping("/food/{dailyStoryFoodId}")
    public void deleteFood(@PathVariable String dailyStoryFoodId) {
        this.dailyStoryFoodService.delete(dailyStoryFoodId);
    }

    private Specification<Food> getFoodSpecification(String name, String type, String category) {
        SpecificationBuilder<Food> specBuilder = new SpecificationBuilderImpl<>();

        specBuilder = specBuilder
                .with(new SearchCriteria("name", "~", name))
                .with(new SearchCriteria("isCustom", ":", type.contains("custom")));

        if (type.contains("custom")) {
            specBuilder = specBuilder
                    .<User>join(new JoinCriteria("user", "username", this.authenticationFacade.getUsername()));
        } else if (type.contains("favorite")) {
            specBuilder = specBuilder
                    .<User>join(new JoinCriteria("users", "username", this.authenticationFacade.getUsername()));
        }

        if (!category.equalsIgnoreCase("all")) {
            specBuilder = specBuilder
                    .<FoodCategory>join(new JoinCriteria("foodCategories", "name", category));
        }

        return specBuilder
                .build(FoodSpecification::new);
    }
}
