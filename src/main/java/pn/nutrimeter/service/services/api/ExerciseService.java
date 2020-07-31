package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.ExerciseServiceModel;

import java.util.List;

public interface ExerciseService {

    List<ExerciseServiceModel> getAllNonCustom();

    ExerciseServiceModel getById(String exerciseId);

    ExerciseServiceModel getByNameAndKcalBurnedPerMin(String name, Double kcal);

    ExerciseServiceModel create(ExerciseServiceModel model, String username);
}
