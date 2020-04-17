package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryFoodService;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.binding.DailyFoodBindingModel;
import pn.nutrimeter.web.models.view.FoodSimpleViewModel;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FoodRestController {

    private final FoodService foodService;

    private final UserService userService;

    private final DailyStoryFoodService dailyStoryFoodService;

    private final ModelMapper modelMapper;

    public FoodRestController(FoodService foodService, UserService userService, DailyStoryFoodService dailyStoryFoodService, ModelMapper modelMapper) {
        this.foodService = foodService;
        this.userService = userService;
        this.dailyStoryFoodService = dailyStoryFoodService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/foods")
    public List<FoodSimpleViewModel> allFoods() {
        return this.foodService.getAll()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodSimpleViewModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/food/{foodId}")
    public void addFood(
            @PathVariable String foodId,
            @RequestBody DailyFoodBindingModel model,
            Principal principal) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(model.getDate(), formatter);
        double quantity = Double.parseDouble(model.getQuantity());
        UserServiceModel userModel = this.userService.getUserByUsername(principal.getName());

        this.dailyStoryFoodService.create(quantity, date, foodId, userModel.getId());
    }
}
