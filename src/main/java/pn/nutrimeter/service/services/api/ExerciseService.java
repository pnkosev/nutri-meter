package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.ExerciseServiceModel;

import java.util.List;

public interface ExerciseService {

    List<ExerciseServiceModel> getAllNonCustom();

    ExerciseServiceModel getById(String exerciseId);

    ExerciseServiceModel getByNameAndKcalBurnedPerHour(String name, Double kcalBurnedPerHour);

    ExerciseServiceModel create(ExerciseServiceModel model, String username);
}
