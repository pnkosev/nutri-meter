package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.service.models.ExerciseServiceModel;
import pn.nutrimeter.service.models.UserServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryExerciseService;
import pn.nutrimeter.service.services.api.ExerciseService;
import pn.nutrimeter.service.services.api.UserService;
import pn.nutrimeter.web.models.binding.DailyExerciseBindingModel;
import pn.nutrimeter.web.models.view.ExerciseViewModel;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ExerciseRestController {

    private final ExerciseService exerciseService;

    private final UserService userService;

    private final DailyStoryExerciseService dailyStoryExerciseService;

    private final ModelMapper modelMapper;

    public ExerciseRestController(ExerciseService exerciseService, UserService userService, DailyStoryExerciseService dailyStoryExerciseService, ModelMapper modelMapper) {
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.dailyStoryExerciseService = dailyStoryExerciseService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/exercises")
    public List<ExerciseViewModel> getExercises() {
        return this.exerciseService
                .getAllNonCustom()
                .stream()
                .map(e -> this.modelMapper.map(e, ExerciseViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/exercise/{exerciseId}")
    public ExerciseViewModel getExercise(@PathVariable String exerciseId) {
        ExerciseServiceModel exerciseServiceModel = this.exerciseService.getById(exerciseId);
        return this.modelMapper.map(exerciseServiceModel, ExerciseViewModel.class);
    }

    @PostMapping("/exercise/add")
    public void addExercise(
            @RequestBody DailyExerciseBindingModel model,
            Principal principal) {

        ExerciseServiceModel esm = this.exerciseService.getByNameAndKcalBurnedPerMin(model.getName(), model.getKcalBurnedPerMin());
        UserServiceModel userModel = this.userService.getUserByUsername(principal.getName());

        if (esm == null) {
            // TODO VALIDATION BEFORE CREATION
            esm = this.exerciseService.create(this.modelMapper.map(model, ExerciseServiceModel.class), userModel.getUsername());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(model.getDate(), formatter);

        this.dailyStoryExerciseService.create(date, userModel.getId(), esm.getId(), model.getDuration());
    }
}
