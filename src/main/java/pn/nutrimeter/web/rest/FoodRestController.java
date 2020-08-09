package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.FoodCategory;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.models.specifications.*;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilderImpl;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilder;
import pn.nutrimeter.error.BaseRuntimeException;
import pn.nutrimeter.error.IdNotFoundException;
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
public class FoodRestController extends BaseRestController {

    private final FoodService foodService;

    private final UserService userService;

    private final MeasureService measureService;

    private final DailyStoryFoodService dailyStoryFoodService;

    private final AuthenticationFacade authenticationFacade;

    private final ModelMapper modelMapper;

    public FoodRestController(FoodService foodService,
                              UserService userService,
                              MeasureService measureService,
                              DailyStoryFoodService dailyStoryFoodService,
                              AuthenticationFacade authenticationFacade,
                              ModelMapper modelMapper) {
        this.foodService = foodService;
        this.userService = userService;
        this.measureService = measureService;
        this.dailyStoryFoodService = dailyStoryFoodService;
        this.authenticationFacade = authenticationFacade;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/foods-all")
    public ResponseEntity<List<FoodSimpleViewModel>> allFoods() {
        List<FoodSimpleViewModel> allNonCustomFoodViewModels = this.foodService
                .getAllNonCustom()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(allNonCustomFoodViewModels);
    }

    @GetMapping("/foods-custom")
    public ResponseEntity<List<FoodSimpleViewModel>> allCustomFoods() {
        List<FoodSimpleViewModel> allCustomFoodViewModels = this.foodService
                .getAllCustomOfUser()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(allCustomFoodViewModels);
    }

    @GetMapping("/foods-favorite")
    public ResponseEntity<List<FoodSimpleViewModel>> allFavoriteFoods() {
        List<FoodSimpleViewModel> favoriteFoodViewModels = this.foodService.getAllFavoritesOfUser()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(favoriteFoodViewModels);
    }

    @PostMapping("/foods-favorite")
    public ResponseEntity toggleFavoriteFood(@RequestBody Map<String, String> payload) {
        String foodId = payload.get("foodId");
        boolean isFavorite = payload.get("isFavorite").equalsIgnoreCase("true");

        // TODO VALIDATION OF PAYLOAD

        if (isFavorite) {
            this.foodService.addFoodAsFavorite(foodId);
        } else {
            this.foodService.removeFoodAsFavorite(foodId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/foods")
    public ResponseEntity<List<FoodSimpleViewModel>> searchedFoods(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "category") String category
    ) {
        // TODO VALIDATION OF PARAMS

        Specification<Food> specification = getFoodSpecification(name, type, category);

        List<FoodSimpleViewModel> searchedFoods = this.foodService
                .getAll(specification)
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(searchedFoods);
    }

    @GetMapping("/food/{foodId}")
    public ResponseEntity<FoodDetailedViewModel> getFood(@PathVariable String foodId) {
        FoodDetailedViewModel foodViewModel =
                this.modelMapper.map(
                        this.foodService.getById(foodId), FoodDetailedViewModel.class
                );

        return ResponseEntity.status(HttpStatus.OK).body(foodViewModel);
    }

    @PostMapping("/food/{foodId}")
    public ResponseEntity addFood(
            @PathVariable String foodId,
            @RequestBody DailyFoodBindingModel model,
            Principal principal) {

        // TODO - VALIDATION OF PARAMS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(model.getDate(), formatter);
        double quantity = Double.parseDouble(model.getQuantity());

        MeasureServiceModel measure = this.measureService.getById(model.getMeasure());
        UserServiceModel userModel = this.userService.getUserByUsername(principal.getName());

        this.dailyStoryFoodService.create(
                measure.getName(), measure.getEquivalentInGrams(), quantity, date, foodId, userModel.getId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/food/{dailyStoryFoodId}")
    public ResponseEntity deleteFood(@PathVariable String dailyStoryFoodId) {
        this.dailyStoryFoodService.delete(dailyStoryFoodId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private Specification<Food> getFoodSpecification(String name, String type, String category) {
        SpecificationBuilder<Food> specBuilder = new SpecificationBuilderImpl<>();

        specBuilder = specBuilder
                .with(new SearchCriteria("name", "~", name))
                .with(new SearchCriteria("isCustom", ":", type.contains("custom")));

        if (type.contains("custom")) {
            specBuilder = specBuilder
                    .<User>join(
                            new JoinCriteria("user", "username", this.authenticationFacade.getUsername())
                    );
        } else if (type.contains("favorite")) {
            specBuilder = specBuilder
                    .<User>join(
                            new JoinCriteria("users", "username", this.authenticationFacade.getUsername())
                    );
        }

        if (!category.equalsIgnoreCase("all")) {
            specBuilder = specBuilder
                    .<FoodCategory>join(new JoinCriteria("foodCategories", "name", category));
        }

        return specBuilder
                .build(FoodSpecification::new);
    }
}
