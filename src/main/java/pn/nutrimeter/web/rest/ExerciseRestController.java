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

        Double kcalBurnedPerHour = this.getKcalBurnedPerHour(model.getDuration(), model.getKcalBurned());

        ExerciseServiceModel esm = this.exerciseService.getByNameAndKcalBurnedPerHour(model.getName(), kcalBurnedPerHour);
        UserServiceModel userModel = this.userService.getUserByUsername(principal.getName());

        if (esm == null) {
            // TODO VALIDATION BEFORE CREATION
            ExerciseServiceModel exerciseServiceModel = this.modelMapper.map(model, ExerciseServiceModel.class);
            exerciseServiceModel.setKcalBurnedPerHour(kcalBurnedPerHour);
            esm = this.exerciseService.create(exerciseServiceModel, userModel.getUsername());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(model.getDate(), formatter);

        this.dailyStoryExerciseService.create(date, userModel.getId(), esm.getId(), model.getDuration());
    }

    @DeleteMapping("/exercise/{dailyStoryExerciseId}")
    public void deleteExercise(@PathVariable String dailyStoryExerciseId) {
        this.dailyStoryExerciseService.delete(dailyStoryExerciseId);
    }

    private Double getKcalBurnedPerHour(Double duration, Double kcalBurned) {
        return kcalBurned / (duration / 60);
    }
}
