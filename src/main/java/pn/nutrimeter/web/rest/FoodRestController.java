package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.specifications.FoodSpecification;
import pn.nutrimeter.data.models.specifications.SearchCriteria;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilderImpl;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilder;
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

    private final ModelMapper modelMapper;

    public FoodRestController(FoodService foodService, UserService userService, MeasureService measureService, DailyStoryFoodService dailyStoryFoodService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.userService = userService;
        this.measureService = measureService;
        this.dailyStoryFoodService = dailyStoryFoodService;
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
    public List<FoodSimpleViewModel> searchedFoods(@RequestParam(value = "name") String name) {
        SpecificationBuilder<Food> fsb = new SpecificationBuilderImpl<>();
        Specification<Food> spec = fsb
                .with(new SearchCriteria("name", "~", name))
                .with(new SearchCriteria("isCustom", ":", false))
                .build(FoodSpecification::new);

        return this.foodService.getAll(spec)
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
}
